document.addEventListener('DOMContentLoaded', () => {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {
			//coordenas de cliente
			var latitude = position.coords.latitude;
			var longitude = position.coords.longitude;


			//instanciar map
			var mymap = L.map('mapaPaciente', {
				center: [latitude, longitude],
				zoom: 12
			});

			L.tileLayer('https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
				maxZoom: 25,
				attribution: 'Datos del mapa de &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a>, ' + '<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' + 'Imágenes © <a href="https://www.mapbox.com/">Mapbox</a>',
				id: 'mapbox/streets-v11'
			}).addTo(mymap);

			axios.get('/proyecto_limpio_spring_war_exploded/api/medicos')
				.then(response => {

					var customIcon = new L.Icon({
						iconUrl: '/proyecto_limpio_spring_war_exploded/imagenes/iconos/doctor.png',
						iconSize: [40, 50]
					});

					response.data.forEach( medico => {
						var markerPaciente = new L.marker([latitude, longitude]).bindPopup("Usted está aquí").openPopup();

						var markerMedico = new L.marker([medico.ubicacion.lat_actual, medico.ubicacion.long_actual], {icon: customIcon})

						var ruta = L.Routing.control({
							waypoints: [
								L.latLng(medico.ubicacion.lat_actual, medico.ubicacion.long_actual),
								L.latLng(latitude, longitude)
							],
							draggableWaypoints: false, // Ruta no editable
							lineOptions : { addWaypoints: false	}, // Ruta no editable
							language: 'es',
							createMarker: function (i, waypoint, number) {
								if (i !== 0){
									return markerMedico
								}else {
									return markerPaciente
								}
							}
						});
						ruta.addTo(mymap);
						ruta.hide(); //minimiza las instrucciones

						//Obtengo tiempo y distancia y lo muestro en un popUp con datos del Medico
						ruta.on('routesfound', function(e) {
							var rutasEncontradas = e.routes;
							var summary = rutasEncontradas[0].summary;

							var distanciaTotal = Math.round(summary.totalDistance / 1000);
							var tiempoEstimado = Math.round(summary.totalTime % 3600 / 60);

							var txtPopUp = "Dr " + medico.persona.apellido + "</br><br>" +
								"Demora: " + tiempoEstimado + " minutos</br><br>Distancia: "
								+ distanciaTotal + " Km</br>";

							markerMedico.bindPopup(txtPopUp).openPopup();
						});

					})
				})
				.catch(error => {
					console.log("error api")
				})
		});
	}
	else {
		console.log("no funca geolocalizacion")
	}

})