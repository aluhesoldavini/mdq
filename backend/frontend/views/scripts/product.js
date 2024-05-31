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
function showSuccessAlert(message){
	const elemAlertSuccess = document.getElementById('alert_success');
	elemAlertSuccess.innerHTML = message;
	elemAlertSuccess.style.display = "block";
	setTimeout(function () {
		elemAlertSuccess.style.display = "none";
	}, 5000);
	console.log(message);
}

function getHeaders(){
	let storedToken = "";
	let myHeaders = {};
	const value = "; " + document.cookie;
    const parts = value.split("; " + "jwt" + "=");
    if(parts.length === 2){
        storedToken = parts.pop().split(";").shift();
		myHeaders = {
			'Content-Type': 'application/json',
			'Authorization': `Bearer ${storedToken}`,
		}
    } else {
		myHeaders = {
			'Content-Type': 'application/json'
		}
	}
	return myHeaders;
}

function setProductFromDB() {
	document.body.style.display = "none";
	const productIdHash = window.location.hash;
	let productId = 0;

	if(!productIdHash.startsWith("#")){
		window.location.href = "/views/404.html";
		return;
	}

	productId = productIdHash.slice(1);
	if(productId === 0 || isNaN(productId) || !Number.isInteger(Number(productId))){
		window.location.href = "/views/404.html";
		return;
	}


	fetch(api + `/product/${productId}`, {
		method: 'GET',
		headers: getHeaders()
	}).then(response => {
		if(response.status === 500) throw new Error("Error interno del servidor");
		return response.json();
	})
	.then(data => {
		if(data.message) showErrorAlert(data.message);
		else {
			document.body.style.display = "block";

			const elemImagesContainer = document.getElementById('product__top__images');
			const elemMainImageContainer = document.getElementById('product__main_image');

			elemImagesContainer.innerHTML = '';
			elemMainImageContainer.innerHTML = '';


			const images = data.imagenes;
			const elemMainImage = document.createElement('img');
			elemMainImage.classList.add('card-img-top');
			elemMainImage.setAttribute('alt', "Error: No se pudo cargar la imagen");
			let lastImage = 0;
			let myBool = true;
			if(images){
				for(let i = 0, l = images.length; i <= l; i++) {
					if(images[i] === ";" || i === images.length){
						let elemImage = document.createElement('img');
						elemImage.classList.add('card-img-top');
						elemImage.setAttribute('src', images.substring(lastImage, i));
						elemImage.setAttribute('alt', "Imagen del producto");

						elemImage.onload = function(){
							let elemImageContainer = document.createElement('div');
							elemImageContainer.classList.add('product__top__images_image');

							elemImageContainer.appendChild(elemImage);
							elemImagesContainer.appendChild(elemImageContainer);

							if(myBool){
								elemMainImage.setAttribute('src', elemImage.src);
								elemMainImage.setAttribute('alt', "Imagen principal del producto");
								myBool = false;
							}

							elemImage.addEventListener('click', function () {
								handleImageClick(elemImage);
							});
						}
						lastImage = i + 1;
					}
				}
			}
			elemMainImageContainer.appendChild(elemMainImage);

			let elemTitle = document.getElementById('product__title');
			elemTitle.textContent = data.nombre.substring(0, 29);

			let elemDesc = document.getElementById('product__description');
			elemDesc.textContent = data.descripcion.substring(0, 1999);
		}
	})
	.catch(error => {
		showErrorAlert(error);
		window.location.href = "/views/500.html";
    });
}
function handleImageClick(elemImage) {
	let link = elemImage.getAttribute('src');
	let elemMainImageContainer = document.getElementById('product__main_image');
	elemMainImageContainer.innerHTML = "";
	let elemMainImage = document.createElement('img');
	elemMainImage.classList.add('card-img-top');
	elemMainImage.setAttribute('src', link);
	elemMainImage.setAttribute('alt', "Imagen principal del producto");
	elemMainImageContainer.appendChild(elemMainImage);
}

window.addEventListener("hashchange", setProductFromDB);

document.addEventListener('DOMContentLoaded', function() {
	setProductFromDB();
});
