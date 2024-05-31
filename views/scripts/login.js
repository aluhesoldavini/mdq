const api = "http://localhost:8080/api/v1";
const elemAlertError = document.getElementById('alert_error');

function showErrorAlert(message){
	const elemAlertError = document.getElementById('alert_error');
	elemAlertError.innerHTML = message;
	elemAlertError.style.display = "block";
	setTimeout(function () {
		elemAlertError.style.display = "none";
	}, 5000);
	console.error(message);
}

function setLinkFromDB(){
	fetch(api + '/contact', {
		method: 'GET',
		headers: {
			'Content-Type': 'application/json'
		}
	}).then(response => {
		if(response.status === 500) throw new Error("Error interno del servidor");
		return response.json();
		})

	.then(data => {
		if(data.message) showErrorAlert(data.message);
		else{
			const elemFormContainer = document.getElementById('login');
			const elemAnchor = elemFormContainer.querySelector('a');
			elemAnchor.setAttribute('href', "https://wa.me/" + data.numCelu);
			elemAnchor.textContent = "¿No tenés cuenta? Contáctanos!";
		}
	})
	.catch(error => {
		showErrorAlert(error);
		window.location.href = "/views/500.html";
	});
}

const button_submit = document.getElementById("login_submit");
button_submit.addEventListener('click', function() {
	const name = document.getElementById("login_name").value;
	const pass = document.getElementById("login_password").value;
	if(name.length < 3 || name.length > 30){
		showErrorAlert("El nombre debe contener entre 3 y 30 caractéres")
		return;
	}
	if(pass.length < 4 || pass.length > 254){
		showErrorAlert("La contraseña debe contener entre 4 y 254 caractéres")
		return;
	}


	fetch(api + '/auth/login', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify({
			"username": name,
			"password": pass
		})
	})
	.then(response => {
		if(response.status === 500) throw new Error("Error interno del servidor");
		return response.json();
	})
	.then(data => {
		if(data.message) showErrorAlert(data.message);
		else {
			const token = data.accessToken;
			const expirationDate = new Date();
			expirationDate.setDate(expirationDate.getDate() + 1);

			const cookieValue = encodeURIComponent(token) + '; expires=' + expirationDate.toUTCString() + '; path=/';

			document.cookie = 'jwt=' + cookieValue;
			window.location.href = "/index.html#mayorista";
		}
	})
	.catch(error => {
		showErrorAlert(error);
		window.location.href = "/views/500.html";
	});
});

document.addEventListener('DOMContentLoaded', function() {
	setLinkFromDB();
});