document.addEventListener('DOMContentLoaded', () => {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
            //coordenas de usuario

            var latitudObtenida = document.getElementById("latitud").innerHTML;
            var longuitudObtenida = document.getElementById("longitud").innerHTML;

            var latitudPaciente = position.coords.latitude;
            var longitudpaciente = position.coords.longitude;

            var latitudPaciente1 = latitudPaciente;
            var longitudpaciente1 = longitudpaciente;

            //instanciar map
            var mymap = L.map('mapaMedicoCitasIndividuales', {
                center: [latitudObtenida, longuitudObtenida],
                zoom: 12
            });
            L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
                maxZoom: 25,
                attribution: 'Datos del mapa de &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a>, ' + '<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' + 'Imágenes © <a href="https://www.mapbox.com/">Mapbox</a>',
                id: 'mapbox/streets-v11'
            }).addTo(mymap);

            //agregar ruta
            L.Routing.control({
                waypoints: [
                    L.latLng(latitudPaciente1, longitudpaciente1), //coordenadas del paciente
                    L.latLng(latitudObtenida, longuitudObtenida) //coordenadas del consultorio
                ],
                language: 'es'
            }).addTo(mymap);
        });
    } else {
        console.log("no funca geolocalizacion")
    }
})