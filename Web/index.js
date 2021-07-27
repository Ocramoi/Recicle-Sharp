let mapa = L.map('mapa',
                {
                    zoomControl: false
                }).setView(
    [0, 0],
    8
);

L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token={accessToken}', {
    attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
    maxZoom: 18,
    id: 'mapbox/streets-v11',
    tileSize: 512,
    zoomOffset: -1,
    accessToken: 'pk.eyJ1Ijoib2NyYW1vaSIsImEiOiJjanZvOHJlcXcxdzEzNDRxcjd1ZWZiNm0wIn0.JMcj2yGoeojqmBKjP6EqKA'
}).addTo(mapa);

L.control.zoom({
    position: 'bottomleft'
}).addTo(mapa);

let reqsPontos = new Request("localhost:8080/data");
fetch(reqsPontos).then((e) => {
    console.log(e.json());
});
