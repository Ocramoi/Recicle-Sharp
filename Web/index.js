// Cria objeto de mapa do leaflet
let mapa = L.map('mapa', {
                    zoomControl: false
                }).setView(
    [0, 0],
    8
);

// Carrega layer de imagem de mapa ao mapa
L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token={accessToken}', {
    attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
    maxZoom: 18,
    id: 'mapbox/streets-v11',
    tileSize: 512,
    zoomOffset: -1,
    accessToken: 'pk.eyJ1Ijoib2NyYW1vaSIsImEiOiJjanZvOHJlcXcxdzEzNDRxcjd1ZWZiNm0wIn0.JMcj2yGoeojqmBKjP6EqKA'
}).addTo(mapa);

// Adiciona controle de zoom
L.control.zoom({
    position: 'bottomleft'
}).addTo(mapa);

// Trandforma array de objetos de ponto para marcadores do mapa com popups corretos
function arToMarkers(regs) {
    for (let r = 0; r < regs.length; ++r) {
        // Adiciona marcador na posição correta
        let marker = L.marker([
            regs[r]["latitude"],
            regs[r]["longitude"]
        ]).addTo(mapa);

        // Cria e formata popup
        marker.bindPopup(`Ponto de coleta de lixo <b>${regs[r]["tipoResiduo"]}</b> do usuário <b>${regs[r]["usr"]}</b><br>` +
                         `Disponível para coleta de ${regs[r]["dias"].join(", ")} às ${regs[r]["horarios"]}.`);
    }
    // Move câmera para o último marcador da lista
    if (regs.length > 0) {
        mapa.panTo(
            [
                regs[regs.length - 1]["latitude"],
                regs[regs.length - 1]["longitude"]
            ]
        )
    }
}

// Faz requisição à API e cria marcadores com a informação coletada
let reqsPontos = new Request("http://127.0.0.1:8080/data");
fetch(reqsPontos).then((e) => {
    e.json().then((j) => {
        console.log(j);
        arToMarkers(j.data)
    });
});
