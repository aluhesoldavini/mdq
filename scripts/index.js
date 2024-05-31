const categoryButtonToggle = document.getElementById ('category__button_toggle');
categoryButtonToggle.addEventListener('click', function () {
	const categoryList = document.getElementById('category__list');
	const computedStyle = window.getComputedStyle(categoryList);
	const categoryButtonIcon1 = document.getElementById('category__button_icon1');
	const categoryButtonIcon2 = document.getElementById('category__button_icon2');
	if(computedStyle.getPropertyValue("height") === "40px"){
		categoryList.style.overflow = "visible";
		categoryList.style.height = "100%";
		categoryButtonIcon1.style.display = "none";
		categoryButtonIcon2.style.display = "block";
	} else {
		categoryList.style.overflow = "hidden";
		categoryList.style.height = "40px";
		categoryButtonIcon1.style.display = "block";
		categoryButtonIcon2.style.display = "none";
	}
});



const api = "http://localhost:8080/api/v1";
const btnNavMinorista = document.getElementById('top__nav_btn_minor');
const btnNavMayorista = document.getElementById('top__nav_btn_mayor');

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

function showErrorAlert(message){
	const elemAlertError = document.getElementById('alert_error');
	elemAlertError.innerHTML = message;
	elemAlertError.style.display = "block";
	setTimeout(function () {
		elemAlertError.style.display = "none";
	}, 5000);
	console.error(message);
}



function setCategoriesFromDB(userType) {
	let myHeaders = {
		'Content-Type': 'application/json',
	}
	if(userType !== 0){
		myHeaders = getHeaders();
	}
	fetch(api + '/category', {
		method: 'GET',
		headers: myHeaders
	}).then(response => {
		if(response.status === 500) throw new Error("Error interno del servidor");
		return response.json();
	}).then(data => {
		if(data.message) showErrorAlert(data.message);
		else {
			let elemUl = document.getElementById("category__list_ul");
			elemUl.innerHTML = "";
			for(let i = 0, l = data.length; i < l; i++){
				const elemLi = document.createElement('li');
				elemLi.textContent = data[i].nombre;
				elemLi.dataset.categoryId = data[i].idCategoria;
				elemUl.appendChild(elemLi);
				elemLi.addEventListener('click', function(){
					getProducts(data[i].idCategoria);
				});
			}
		}
	}).catch(error => {
		showErrorAlert(error);
		window.location.href = "/views/500.html";
	});
}

function setContactFromDB(){
	fetch(api + '/contact', {
		method: 'GET',
		headers: getHeaders()
	}).then(response => {
		if(response.status === 500) throw new Error("Error interno del servidor");
		return response.json();
	}).then(data => {
		if(data.message) showErrorAlert(data.message);
		else{
			const elemButton = document.getElementById('contact__button');
			const elemAnchor = document.createElement('a');
			elemAnchor.setAttribute('href', "https://wa.me/" + data.numCelu);
			elemAnchor.setAttribute('target', '_blank');
			elemAnchor.textContent = "WhatsApp";
			elemButton.appendChild(elemAnchor);
		}
	}).catch(error => {
		showErrorAlert(error);
		window.location.href = "/views/500.html";
	});
}

function setAdminButton(){
	fetch(api + '/auth/checkrole', {
		method: 'GET',
		headers: getHeaders()
	}).then(response => {
		if(response.status === 500) setContactFromDB();
		return response.json();
	}).then(data => {
		if(data.message) showErrorAlert(data.message);
		else {
			if(data <= 1) setContactFromDB();
			else {
				const elemButton = document.getElementById('contact__button');
				const elemAnchor = document.createElement('a');
				elemButton.style.backgroundColor = '#3559E0';
				elemAnchor.setAttribute('href', "/views/admin.html");
				elemAnchor.textContent = "Administrar";
				elemButton.appendChild(elemAnchor);
			}
		}
	}).catch(error => {
		showErrorAlert(error);
		window.location.href = "/views/500.html";
	});
}



function getProducts(catId){
	if(catId !== -2){
		btnNavMinorista.style.setProperty("background-color", "#008c9e");
		btnNavMayorista.style.setProperty("background-color", "#00e1ff");
		if(catId === -1){
			btnNavMinorista.style.setProperty("background-color", "#00e1ff");
			btnNavMayorista.style.setProperty("background-color", "#008c9e");
		}
		let myHeaders = {
			'Content-Type': 'application/json',
		}
		if(catId !== 0){
			myHeaders = getHeaders();
		}
		fetch(api + '/product', {
			method: 'GET',
			headers: myHeaders
		}).then(response => {
			if(response.status === 500) throw new Error("Error interno del servidor");
			return response.json();
		}).then(data => {
			if(data.message) showErrorAlert(data.message);
			else setProductsFromDB(data, catId);
		}).catch(error => {
			showErrorAlert(error);
			window.location.href = "/views/500.html";
		});
	} else {
		const searchInput = document.getElementById("search__bar_box");
		if(!searchInput.value){
			return;
		}
		fetch(api + `/product/contains/${searchInput.value}`, {
			method: 'GET',
			headers: getHeaders()
		}).then(response => {
			if(response.status === 500) throw new Error("Error interno del servidor");
			return response.json();
		}).then(data => {
			if(data.message) showErrorAlert(data.message);
			else {
				setProductsFromDB(data, catId);
				searchInput.value = "";
			}
		}).catch(error => {
			showErrorAlert(error);
			window.location.href = "/views/500.html";
		});
	}
}

function setProductsFromDB(data, catId){
	const elemContainer = document.getElementById("product__container");
	elemContainer.innerHTML = "";
	let myBool = true;

	for(let i = 0, datalength = data.length; i < datalength; i++){
		if(!data[i].stock) continue;
		if(catId === 0 || catId === -1){
			if(!data[i].recomendado) continue;
		}
		else if(catId !== -2 && data[i].category.idCategoria !== catId){
			continue;
		}

		const imagenes = data[i].imagenes;
		const elemImage = document.createElement('img');
		elemImage.classList.add('card-img-top');
		let firstImage = "";

		if(imagenes){
			for(let j = 0, imglenght = imagenes.length; j < imglenght; j++) {
				if(imagenes[j] === ";"){
					firstImage = imagenes.substring(0, j);
					break;
				}
			}
		}
		elemImage.setAttribute('src', firstImage);
		elemImage.setAttribute('alt', "Imagen del producto");
		const elemParagraph = document.createElement('p');
		elemParagraph.classList.add('card-text', 'product__card_text');
		elemParagraph.textContent = data[i].nombre;
		const elemDivText = document.createElement('div');
		elemDivText.classList.add('card-body');
		elemDivText.appendChild(elemParagraph);
		const elemDivAll = document.createElement('div');
		elemDivAll.classList.add('card', 'product__card');
		elemDivAll.dataset.productId = data[i].idProducto;
		elemDivAll.appendChild(elemImage);
		elemDivAll.appendChild(elemDivText);
		elemContainer.appendChild(elemDivAll);
		elemDivAll.addEventListener('click', function() {
			const productId = this.dataset.productId;
			window.location.href = `/views/product.html#${productId}`;
		});
		if(myBool) myBool = false;
	}
	if(myBool) showErrorAlert("No hay productos en esta categoría");
}

const btnSearch = document.getElementById("search__bar_button");
btnSearch.addEventListener('click', function() {
	getProducts(-2);
});



btnNavMinorista.addEventListener('click', function() {
	setCategoriesFromDB(0);
	getProducts(0);
});
btnNavMayorista.addEventListener('click', function() {
	fetch(api + '/auth/checkrole', {
		method: 'GET',
		headers: getHeaders()
	}).then(response => {
		if(response.status === 500) throw new Error("Error interno del servidor");
		return response.json();
	}).then(data => {
		if(data.message) showErrorAlert(data.message);
		else {
			if(data === 0) window.location.href = "/views/login.html";
			else {
				getProducts(-1);
				setCategoriesFromDB(-1);
			}
		}
	}).catch(error => {
		showErrorAlert(error);
		window.location.href = "/views/500.html";
	});
})



document.addEventListener('DOMContentLoaded', function() {
	if(window.location.hash === "#mayorista"){
		setCategoriesFromDB(-1);
		getProducts(-1);
	} else {
		setCategoriesFromDB(0);
		getProducts(0);
	}
	setAdminButton();
});
