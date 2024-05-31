const btnCreateCategory 	= document.getElementById('btn__create_category');
const btnEditCategorySearch = document.getElementById('btn__edit_category_search');
const btnEditCategory 		= document.getElementById('btn__edit_category');
const btnDeleteCategory 	= document.getElementById('btn__delete_category');

const btnCreateProduct 		= document.getElementById('btn__create_product');
const btnEditProductSearch 	= document.getElementById('btn__edit_product_search');
const btnEditProduct 		= document.getElementById('btn__edit_product');
const btnDeleteProduct 		= document.getElementById('btn__delete_product');

const btnCreateUser 		= document.getElementById('btn__create_user');
const btnEditUserSearch 	= document.getElementById('btn__edit_user_search');
const btnEditUser 			= document.getElementById('btn__edit_user');
const btnDeleteUser 		= document.getElementById('btn__delete_user');

const btnEditContact 		= document.getElementById('btn__edit_contact');
const btnLogout 			= document.getElementById('btn__logout');

const catEditBefore			= document.getElementById('category__edit_before');
const catEditAfter 			= document.getElementById('category__edit_after');
const catEditNameInput 		= document.getElementById('category__edit_name');
const catEditMinor 			= document.getElementById('category__edit_minorista');
const catEditMayor 			= document.getElementById('category__edit_mayorista');

const contEditPhone 		= document.getElementById('contact__edit_phone');
const contEditEmail 		= document.getElementById('contact__edit_email');
const contEditDir		 	= document.getElementById('contact__edit_location');
const contEditDesc		 	= document.getElementById('contact__edit_description');
const contEditSocial1 		= document.getElementById('contact__edit_social1');
const contEditSocialText1 	= document.getElementById('contact__edit_social1_text');
const contEditSocial2 		= document.getElementById('contact__edit_social2');
const contEditSocialText2 	= document.getElementById('contact__edit_social2_text');
const contEditSocial3 		= document.getElementById('contact__edit_social3');
const contEditSocialText3 	= document.getElementById('contact__edit_social3_text');

const userEditBefore		= document.getElementById('user__edit_before');
const userEditAfter			= document.getElementById('user__edit_after');
const userEditNameInput		= document.getElementById('user__edit_name');
const userEditPassInput		= document.getElementById('user__edit_pass');
const userEditContactInput	= document.getElementById('user__edit_contact');
const userEditTypeInput		= document.getElementById('user__edit_type');

const prodCatInput			= document.getElementById('product__create_category');
const prodMinInput			= document.getElementById('product__create_minorista');
const prodMayInput			= document.getElementById('product__create_mayorista');
const prodNameInput 		= document.getElementById("product__create_name");
const prodDescInput 		= document.getElementById('product__create_description');
const checkStockInput	 	= document.getElementById('product__create_stock');
const checkRecomInput 		= document.getElementById('product__create_suggest');
const prodImg1Input 		= document.getElementById('product__create_img1');
const prodImg2Input 		= document.getElementById('product__create_img2');
const prodImg3Input		 	= document.getElementById('product__create_img3');
const prodImg4Input 		= document.getElementById('product__create_img4');
const prodImg5Input 		= document.getElementById('product__create_img5');


const prodEditBefore		= document.getElementById('product__edit_before');
const prodEditAfter 		= document.getElementById('product__edit_after');
const prodEditCatInput		= document.getElementById('product__edit_category');
const prodEditNameInput		= document.getElementById('product__edit_name');
const prodEditDescInput		= document.getElementById('product__edit_description');
const prodEditStockInput	= document.getElementById('product__edit_stock');
const prodEditMinInput		= document.getElementById('product__edit_minorista');
const prodEditMayInput		= document.getElementById('product__edit_mayorista');
const prodEditRecomInput	= document.getElementById('product__edit_suggest');
const prodEditImg1Input		= document.getElementById('product__edit_img1');
const prodEditImg2Input		= document.getElementById('product__edit_img2');
const prodEditImg3Input		= document.getElementById('product__edit_img3');
const prodEditImg4Input		= document.getElementById('product__edit_img4');
const prodEditImg5Input		= document.getElementById('product__edit_img5');

const prodCatNameCreateContainerInput = document.getElementById("product__category__create_text_after");
const prodCatNameEditContainerInput = document.getElementById("product__category__edit_text_after");

function showElement(event) {
	const selectedValue = event.target.value;

	if(selectedValue === "user" ||
	selectedValue === "category" ||
	selectedValue === "product" ||
	selectedValue === "contact"){
		return;
	}
	else {
		catEditBefore.style.display = "block";
		catEditAfter.style.display = "none";
		userEditBefore.style.display = "block";
		userEditAfter.style.display = "none";
		prodEditBefore.style.display = "block";
		prodEditAfter.style.display = "none";

		const formElements = document.querySelectorAll(".form__element");
		formElements.forEach(elem => elem.style.display = "none");

		document.getElementById(selectedValue).style.display = "block";
		if(selectedValue === "product__create") prodCatNameCreateContainerInput.style.display = "block";

		const selectElements = document.querySelectorAll(".crud__select_specific");
		selectElements.forEach(elem => elem.selectedIndex = 0);
	}
}
function showElementCategory(event) {
	const selectedValue = event.target.value;

	if(selectedValue === "0"){
		prodCatNameCreateContainerInput.style.display = "block";
		prodCatNameEditContainerInput.style.display = "block";
	}
	else {
		prodCatNameCreateContainerInput.style.display = "none";
		prodCatNameEditContainerInput.style.display = "none";
	}
}



const api = "http://localhost:8080/api/v1";

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
function showSuccessAlert(message){
	const elemAlertSuccess = document.getElementById('alert_success');
	elemAlertSuccess.innerHTML = message;
	elemAlertSuccess.style.display = "block";
	setTimeout(function () {
		elemAlertSuccess.style.display = "none";
	}, 5000);
	console.log(message);
}

function setProductCategory() {
	prodCatInput.innerHTML = "";
	const elemOptionN = document.createElement('option');
	elemOptionN.value = 0;
	elemOptionN.textContent = "Nueva";
	prodCatInput.appendChild(elemOptionN);
	prodCatNameCreateContainerInput.style.display = "block";
	prodCatNameEditContainerInput.style.display = "block";

	fetch(api + '/category', {
		method: 'GET',
		headers: getHeaders()
	}).then(response => {
		if(response.status === 500) throw new Error("Error interno del servidor");
		return response.json();
	}).then(data => {
		if(data.message) showErrorAlert(data.message);
		else {
			for(let i = 0, l = data.length; i < l; i++){
				const category = data[i];
				const isMinorChecked = prodMinInput.checked;
				const isMayorChecked = prodMayInput.checked;

				if ((!isMinorChecked && !isMayorChecked) ||
					(isMinorChecked && !category.minorista) ||
					(isMayorChecked && !category.mayorista)) {
					continue;
				}

				const elemOption = document.createElement('option');
				elemOption.value = category.idCategoria;
				elemOption.textContent = category.nombre;

				prodCatInput.appendChild(elemOption);
			}
		}
	}).catch(error => {
		showErrorAlert(error);
		window.location.href = "/views/500.html";
	});
}





let catToUpdateId, prodToUpdateId, userToUpdateId = 0;

btnCreateCategory.addEventListener('click', function () {
	const check1 = document.getElementById('category__create_minorista');
	const check2 = document.getElementById('category__create_mayorista');
	const catNameInput = document.getElementById("category__create_name");
	if(!check1.checked && !check2.checked){
		showErrorAlert("La categoría debe ser minorista, mayorista o ambos");
		return;
	}
	const catName = catNameInput.value.trim();
	if(catName.length < 3 || catName.length > 15){
        showErrorAlert("El nombre de la categoría debe contener entre 3 y 15 caractéres");
		return;
    } if(!/^[a-zA-ZñÑáéíóúÁÉÍÓÚ\d\s]+$/.test(catName)){
        showErrorAlert("El nombre de la categoría solo puede contener letras, numeros y espacios");
		return;
    }


	fetch(api + '/category/create', {
		method: 'POST',
		headers: getHeaders(),
		body: JSON.stringify({
			"nombre": catName,
			"minorista": check1.checked,
			"mayorista": check2.checked,
		})
	}).then(response => {
		if(response.status === 500) throw new Error("Error interno del servidor");
		return response.json();
	}).then(data => {
		if(data.message) showErrorAlert(data.message);
		else {
			showSuccessAlert("Categoría creada exitosamente");
			check1.checked = false;
			check2.checked = false;
			catNameInput.value = "";
		}
	}).catch(error => {
		showErrorAlert(error);
		window.location.href = "/views/500.html";
	});
});

btnEditCategorySearch.addEventListener('click', function () {
	const catNameInput = document.getElementById("category__edit_search_name");
	const catName = catNameInput.value.trim();


	fetch(api + `/category/${catName}`, {
		method: 'GET',
		headers: getHeaders(),
	}).then(response => {
		if(response.status === 500) throw new Error("Error interno del servidor");
		return response.json();
	}).then(data => {
		if(data.message) showErrorAlert(data.message);
		else {
			catEditNameInput.value = data.nombre;
			catEditMinor.checked = data.minorista;
			catEditMayor.checked = data.mayorista;
			catEditBefore.style.display = "none";
			catEditAfter.style.display = "block";
			catToUpdateId = data.idCategoria;
		}
	}).catch(error => {
		showErrorAlert(error);
		window.location.href = "/views/500.html";
	});
});

btnEditCategory.addEventListener('click', function () {
	fetch(api + '/category/update', {
		method: 'PUT',
		headers: getHeaders(),
		body: JSON.stringify({
			"idCategoria": catToUpdateId,
			"nombre": catEditNameInput.value,
			"minorista": catEditMinor.checked,
			"mayorista": catEditMayor.checked
		})
	}).then(response => {
		if(response.status === 500) throw new Error("Error interno del servidor");
		return response.json();
	}).then(data => {
		if(data.message) showErrorAlert(data.message);
		else {
			showSuccessAlert("Categoría actualizada exitosamente");
		}
	}).catch(error => {
		showErrorAlert(error);
		window.location.href = "/views/500.html";
	});
});

btnDeleteCategory.addEventListener('click', function () {
	const catNameInput = document.getElementById("category__delete_name");
	const catName = catNameInput.value.trim();


	fetch(api + `/category/${catName}`, {
		method: 'GET',
		headers: getHeaders(),
	}).then(response => {
		if(response.status === 500) throw new Error("Error interno del servidor");
		return response.json();
	}).then(data => {
		if(data.message) showErrorAlert(data.message);
		else {
			fetch(api + `/category/delete/${data.idCategoria}`, {
				method: 'DELETE',
				headers: getHeaders(),
			}).then(response => {
				if(response.status === 500) throw new Error("Error interno del servidor");
				return response.json();
			}).then(data => {
				if(data.message) showErrorAlert(data.message);
				else {
					showSuccessAlert("Categoría eliminada exitosamente");
					catNameInput.value = "";
				}
			}).catch(error => {
				showErrorAlert(error);
				window.location.href = "/views/500.html";
			});
		}
	}).catch(error => {
		showErrorAlert(error);
		window.location.href = "/views/500.html";
	});
});



btnCreateProduct.addEventListener('click', function () {
	const prodCatNameInput = document.getElementById("product__create_new_category");
	if(!prodMinInput.checked && !prodMayInput.checked){
		showErrorAlert("El producto debe ser minorista, mayorista o ambos");
		return;
	}

	let prodIdCategoria = Number(prodCatInput.value);
	if(prodIdCategoria === 0){
		const prodCatName = prodCatNameInput.value.trim();
		if(prodCatName.length < 3 || prodCatName.length > 15){
			showErrorAlert("El nombre de la categoría debe contener entre 3 y 15 caractéres");
			return;
		} if(!/^[a-zA-ZñÑ\d\s]+$/.test(prodCatName)){
			showErrorAlert("El nombre de la categoría solo puede contener letras, numeros y espacios");
			return;
		}


		fetch(api + '/category/create', {
			method: 'POST',
			headers: getHeaders(),
			body: JSON.stringify({
				"nombre": prodCatName,
				"minorista": prodMinInput.checked,
				"mayorista": prodMayInput.checked
			})
		}).then(response => {
			if(response.status === 500) throw new Error("Error interno del servidor");
			return response.json();
		}).then(data => {
			if(data.message) showErrorAlert(data.message);
			else {
				prodCatNameInput.value = "";
				prodCatInput.innerHTML = "";
				const elemOptionN = document.createElement('option');
				elemOptionN.value = 0;
				elemOptionN.textContent = "Nueva";
				prodCatInput.appendChild(elemOptionN);
				prodCatInput.value = 0;

				prodIdCategoria = data.idCategoria;
				createProduct(prodIdCategoria);
			}
		}).catch(error => {
			showErrorAlert(error);
			window.location.href = "/views/500.html";
		});
	} else createProduct(prodIdCategoria);
});
function createProduct(prodIdCategoria) {
	const prodName = prodNameInput.value.trim();
	const prodDesc = prodDescInput.value.trim();
	const prodImg1 = prodImg1Input.value.trim();
	const prodImg2 = prodImg2Input.value.trim();
	const prodImg3 = prodImg3Input.value.trim();
	const prodImg4 = prodImg4Input.value.trim();
	const prodImg5 = prodImg5Input.value.trim();

	let prodImages = [prodImg1, prodImg2, prodImg3, prodImg4, prodImg5]
    .filter(img => img !== "").join(";");

	if(prodName.length < 3 || prodName.length > 30){
        showErrorAlert("El nombre del producto debe contener entre 3 y 30 caractéres");
		return;
    } if(prodImages){
		if(!prodImages.includes(";")) prodImages += ";";
		if(prodImages.length < 10 || prodImages.length > 500){
			showErrorAlert("Las imágenes deben contener entre 10 y 100 caractéres");
			return;
		}
	} if(prodDesc.length < 3 || prodDesc.length > 2000){
		showErrorAlert("La descripción debe contener entre 3 y 2000 caractéres");
		return;
	}

	let myBody = {
		"category": {
			"idCategoria": prodIdCategoria
		},
		"nombre": prodName,
		"stock": checkStockInput.checked,
		"minorista": prodMinInput.checked,
		"mayorista": prodMayInput.checked,
		"descripcion": prodDesc,
		"recomendado": checkRecomInput.checked,
		"opinion": 0,
		"cantVotos": 0
	}
	if(prodImages){
		myBody = {
			...myBody,
			"imagenes": prodImages
		}
	}

	fetch(api + '/product/create', {
		method: 'POST',
		headers: getHeaders(),
		body: JSON.stringify(myBody)
	}).then(response => {
		if(response.status === 500) throw new Error("Error interno del servidor");
		return response.json();
	}).then(data => {
		if(data.message) showErrorAlert(data.message);
		else {
			showSuccessAlert("Producto creado exitosamente");
			prodCatInput.value = 0;
			prodNameInput.value = "";
			prodDescInput.value = "";
			checkStockInput.checked = false;
			prodMinInput.checked = false;
			prodMayInput.checked = false;
			checkRecomInput.checked = false;
			prodImg1Input.value = "";
			prodImg2Input.value = "";
			prodImg3Input.value = "";
			prodImg4Input.value = "";
			prodImg5Input.value = "";
		}
	}).catch(error => {
		showErrorAlert(error);
		window.location.href = "/views/500.html";
	});
}

btnEditProductSearch.addEventListener('click', function () {
	const prodNameInput = document.getElementById("product__edit_search_name");
	const prodName = prodNameInput.value.trim();


	fetch(api + `/product/${prodName}`, {
		method: 'GET',
		headers: getHeaders(),
	}).then(response => {
		if(response.status === 500) throw new Error("Error interno del servidor");
		return response.json();
	}).then(data => {
		if(data.message) showErrorAlert(data.message);
		else {
			prodEditCatInput.value = "";
			prodEditCatInput.innerHTML = "";

			const elemOptionNew = document.createElement('option');
			elemOptionNew.value = 0;
			elemOptionNew.textContent = "Nueva";
			prodEditCatInput.appendChild(elemOptionNew);

			const elemOptionN = document.createElement('option');
			elemOptionN.value = data.category.idCategoria;
			elemOptionN.textContent = data.category.nombre;
			prodEditCatInput.appendChild(elemOptionN);

			prodEditCatInput.value = data.category.idCategoria;

			prodEditNameInput.value = data.nombre;
			prodEditDescInput.value = data.descripcion;
			prodEditStockInput.checked = data.stock;
			prodEditMinInput.checked = data.minorista;
			prodEditMayInput.checked = data.mayorista;
			prodEditRecomInput.checked = data.recomendado;

			const imageArray = data.imagenes.split(";");
			prodEditImg1Input.value = imageArray[0] || '';
			prodEditImg2Input.value = imageArray[1] || '';
			prodEditImg3Input.value = imageArray[2] || '';
			prodEditImg4Input.value = imageArray[3] || '';
			prodEditImg5Input.value = imageArray[4] || '';

			prodCatNameEditContainerInput.style.display = "none";

			prodEditBefore.style.display = "none";
			prodEditAfter.style.display = "block";

			prodToUpdateId = data.idProducto;
		}
	}).catch(error => {
		showErrorAlert(error);
		window.location.href = "/views/500.html";
	});
});

btnEditProduct.addEventListener('click', function () {
	const prodEditCatNameInput = document.getElementById("product__edit_new_category");
	if(!prodEditMinInput.checked && !prodEditMayInput.checked){
		showErrorAlert("El producto debe ser minorista, mayorista o ambos");
		return;
	}

	let prodEditIdCategoria = Number(prodEditCatInput.value);
	if(prodEditIdCategoria === 0){
		const prodEditCatName = prodEditCatNameInput.value.trim();
		if(prodEditCatName.length < 3 || prodEditCatName.length > 15){
			showErrorAlert("El nombre de la categoría debe contener entre 3 y 15 caractéres");
			return;
		} if(!/^[a-zA-ZñÑ\d\s]+$/.test(prodEditCatName)){
			showErrorAlert("El nombre de la categoría solo puede contener letras, numeros y espacios");
			return;
		}


		fetch(api + '/category/create', {
			method: 'POST',
			headers: getHeaders(),
			body: JSON.stringify({
				"nombre": prodEditCatName,
				"minorista": prodEditMinInput.checked,
				"mayorista": prodEditMayInput.checked
			})
		}).then(response => {
			if(response.status === 500) throw new Error("Error interno del servidor");
			return response.json();
		}).then(data => {
			if(data.message) showErrorAlert(data.message);
			else {
				prodEditCatNameInput.value = "";
				prodEditCatInput.innerHTML = "";
				const elemOptionN = document.createElement('option');
				elemOptionN.value = 0;
				elemOptionN.textContent = "Nueva";
				prodEditCatInput.appendChild(elemOptionN);
				prodEditCatInput.value = 0;

				prodEditIdCategoria = data.idCategoria;
				editProduct(prodEditIdCategoria);
			}
		}).catch(error => {
			showErrorAlert(error);
			window.location.href = "/views/500.html";
		});
	} else editProduct(prodEditIdCategoria);
});
function editProduct(prodEditIdCategoria){
	const prodEditName = prodEditNameInput.value.trim();
	const prodEditDesc = prodEditDescInput.value.trim();
	const prodEditStock = prodEditStockInput.checked;
	const prodEditMin = prodEditMinInput.checked;
	const prodEditMay = prodEditMayInput.checked;
	const prodEditRecom = prodEditRecomInput.checked;
	const prodEditImg1 = prodEditImg1Input.value.trim();
	const prodEditImg2 = prodEditImg2Input.value.trim();
	const prodEditImg3 = prodEditImg3Input.value.trim();
	const prodEditImg4 = prodEditImg4Input.value.trim();
	const prodEditImg5 = prodEditImg5Input.value.trim();

	const prodImages = [prodEditImg1, prodEditImg2, prodEditImg3, prodEditImg4, prodEditImg5]
    .filter(img => img !== "").join(";");

	if(prodEditName.length < 3 || prodEditName.length > 30){
        showErrorAlert("El nombre del producto debe contener entre 3 y 30 caractéres");
		return;
    } if(prodImages){
		if(prodImages.length < 10 || prodImages.length > 500){
			showErrorAlert("Las imágenes deben contener entre 10 y 100 caractéres");
			return;
		}
	} if(prodEditDesc.length < 3 || prodEditDesc.length > 200){
		showErrorAlert("La descripción debe contener entre 3 y 200 caractéres");
		return;
	}

	let myBody = {
		"category": {
			"idCategoria": prodEditIdCategoria
		},
		"idProducto": prodToUpdateId,
		"nombre": prodEditName,
		"stock": prodEditStock,
		"minorista": prodEditMin,
		"mayorista": prodEditMay,
		"descripcion": prodEditDesc,
		"recomendado": prodEditRecom
	}
	if(prodImages){
		myBody = {
			...myBody,
			"imagenes": prodImages
		}
	}


	fetch(api + '/product/update', {
		method: 'PUT',
		headers: getHeaders(),
		body: JSON.stringify(myBody)
	}).then(response => {
		if(response.status === 500) throw new Error("Error interno del servidor");
		return response.json();
	}).then(data => {
		if(data.message) showErrorAlert(data.message);
		else {
			showSuccessAlert("Producto actualizado exitosamente");
		}
	}).catch(error => {
		showErrorAlert(error);
		window.location.href = "/views/500.html";
	});
}

btnDeleteProduct.addEventListener('click', function () {
	const prodNameInput = document.getElementById("product__delete_name");
	const prodName = prodNameInput.value.trim();


	fetch(api + `/product/${prodName}`, {
		method: 'GET',
		headers: getHeaders(),
	}).then(response => {
		if(response.status === 500) throw new Error("Error interno del servidor");
		return response.json();
	}).then(data => {
		if(data.message) showErrorAlert(data.message);
		else {
			fetch(api + `/product/delete/${data.idProducto}`, {
				method: 'DELETE',
				headers: getHeaders(),
			}).then(response => {
				if(response.status === 500) throw new Error("Error interno del servidor");
				return response.json();
			}).then(data => {
				if(data.message) showErrorAlert(data.message);
				else {
					showSuccessAlert("Producto eliminado exitosamente");
					prodNameInput.value = "";
				}
			}).catch(error => {
				showErrorAlert(error);
				window.location.href = "/views/500.html";
			});
		}
	}).catch(error => {
		showErrorAlert(error);
		window.location.href = "/views/500.html";
	});
});



btnCreateUser.addEventListener('click', function () {
	const userNameInput = document.getElementById("user__create_name");
	const userPassInput = document.getElementById("user__create_pass");
	const userContactInput = document.getElementById("user__create_contact");
	const userTypeInput = document.getElementById("user__create_type");
	const userName = userNameInput.value.trim();
	const userPass = userPassInput.value.trim();
	const userContact = userContactInput.value.trim();
	const userType = userTypeInput.value;
	if(userName.length < 3 || userName.length > 30){
		showErrorAlert("El nombre del usuario debe contener entre 3 y 30 caractéres")
		return;
	} if(userPass.length < 4 || userPass.length > 255){
		showErrorAlert("La contraseña del usuario debe contener entre 4 y 255 caractéres")
		return;
	} if(userContact){
		if(userContact.length < 3 || userContact.length > 30){
			showErrorAlert("El contacto del usuario debe contener entre 3 y 50 caractéres")
			return;
		}
	} if(/\d/.test(userName)){
		showErrorAlert("El nombre del usuario no puede contener números")
		return;
	}


	fetch(api + '/user/create', {
		method: 'POST',
		headers: getHeaders(),
		body: JSON.stringify({
			"nombre": userName,
			"contacto": userContact,
			"contrasena": userPass,
			"tipo": userType
		})
	}).then(response => {
		if(response.status === 500) throw new Error("Error interno del servidor");
		return response.json();
	}).then(data => {
		if(data.message) showErrorAlert(data.message);
		else {
			showSuccessAlert("Usuario creado exitosamente");
			userNameInput.value = "";
			userPassInput.value = "";
			userContactInput.value = "";
			userTypeInput.value = 1;
		}
	}).catch(error => {
		showErrorAlert(error);
		window.location.href = "/views/500.html";
	});
});

btnEditUserSearch.addEventListener('click', function () {
	const userNameInput = document.getElementById("user__edit_search_name");
	const userName = userNameInput.value.trim();


	fetch(api + `/user/${userName}`, {
		method: 'GET',
		headers: getHeaders(),
	}).then(response => {
		if(response.status === 500) throw new Error("Error interno del servidor");
		return response.json();
	}).then(data => {
		if(data.message) showErrorAlert(data.message);
		else {
			userEditNameInput.value = data.nombre;
			userEditContactInput.value = data.contacto;
			userEditTypeInput.value = data.tipo;
			userEditBefore.style.display = "none";
			userEditAfter.style.display = "block";
			userToUpdateId = data.idUsuario;
		}
	}).catch(error => {
		showErrorAlert(error);
		window.location.href = "/views/500.html";
	});
});

btnEditUser.addEventListener('click', function () {
	const userName = userEditNameInput.value.trim();
	const userPass = userEditPassInput.value.trim();
	const userContact = userEditContactInput.value.trim();
	if(userName.length < 3 || userName.length > 30){
		showErrorAlert("El nombre del usuario debe contener entre 3 y 30 caractéres")
		return;
	} if(/\d/.test(userName)){
		showErrorAlert("El nombre del usuario no puede contener números")
		return;
	}

	let myBody = {
		"idUsuario": userToUpdateId,
		"nombre": userName,
		"tipo": userEditTypeInput.value
	};
	if(userPass){
		if(userPass.length < 4 || userPass.length > 255){
			showErrorAlert("La contraseña del usuario debe contener entre 4 y 255 caractéres")
			return;
		}
		myBody = {
			...myBody,
			"contrasena": userPass
		};
	}
	if(userContact){
		if(userContact.length < 3 || userContact.length > 30){
			showErrorAlert("El contacto del usuario debe contener entre 3 y 50 caractéres")
			return;
		}
		myBody = {
			...myBody,
			"contacto": userContact
		};
	}


	fetch(api + '/user/update', {
		method: 'PUT',
		headers: getHeaders(),
		body: JSON.stringify(myBody)
	}).then(response => {
		if(response.status === 500) throw new Error("Error interno del servidor");
		return response.json();
	}).then(data => {
		if(data.message) showErrorAlert(data.message);
		else {
			showSuccessAlert("Usuario actualizado exitosamente");
		}
	}).catch(error => {
		showErrorAlert(error);
		window.location.href = "/views/500.html";
	});
});

btnDeleteUser.addEventListener('click', function () {
	const userNameInput = document.getElementById("user__delete_name");
	const userName = userNameInput.value.trim();


	fetch(api + `/user/${userName}`, {
		method: 'GET',
		headers: getHeaders(),
	}).then(response => {
		if(response.status === 500) throw new Error("Error interno del servidor");
		return response.json();
	}).then(data => {
		if(data.message) showErrorAlert(data.message);
		else {
			fetch(api + `/user/delete/${data.idUsuario}`, {
				method: 'DELETE',
				headers: getHeaders(),
			}).then(response => {
				if(response.status === 500) throw new Error("Error interno del servidor");
				return response.json();
			}).then(data => {
				if(data.message) showErrorAlert(data.message);
				else {
					showSuccessAlert("Usuario eliminado exitosamente");
					userNameInput.value = "";
				}
			}).catch(error => {
				showErrorAlert(error);
				window.location.href = "/views/500.html";
			});
		}
	}).catch(error => {
		showErrorAlert(error);
		window.location.href = "/views/500.html";
	});
});



function setEditContact() {
	fetch(api + `/contact`, {
		method: 'GET',
		headers: getHeaders(),
	}).then(response => {
		if(response.status === 500) throw new Error("Error interno del servidor");
		return response.json();
	}).then(data => {
		if(data.message) showErrorAlert(data.message);
		else {
			contEditPhone.value = data.numCelu;
			contEditEmail.value = data.email;
			contEditDir.value = data.direccion;
			contEditDesc.value = data.info;

			for(let i = 0, l = data.link1.length; i < l; i++){
				if(data.link1[i] === ";"){
					contEditSocial1.value = data.link1.substring(i + 1, l);
					contEditSocialText1.value =  data.link1.substring(0, i);
				}
			}
			for(let i = 0, l = data.link2.length; i < l; i++){
				if(data.link2[i] === ";"){
					contEditSocial2.value = data.link2.substring(i + 1, l);
					contEditSocialText2.value =  data.link2.substring(0, i);
				}
			}
			for(let i = 0, l = data.link3.length; i < l; i++){
				if(data.link3[i] === ";"){
					contEditSocial3.value = data.link3.substring(i + 1, l);
					contEditSocialText3.value =  data.link3.substring(0, i);
				}
			}
		}
	}).catch(error => {
		showErrorAlert(error);
		window.location.href = "/views/500.html";
	});
}

btnEditContact.addEventListener('click', function () {
	let contactLink1 = "";
	if(contEditSocialText1.value && contEditSocialText1.value){
		contactLink1 = contEditSocialText1.value + ";" + contEditSocial1.value;
	}
	let contactLink2 = "";
	if(contEditSocialText2.value && contEditSocialText2.value){
		contactLink2 = contEditSocialText2.value + ";" + contEditSocial2.value;
	}
	let contactLink3 = "";
	if(contEditSocialText3.value && contEditSocialText3.value){
		contactLink3 = contEditSocialText3.value + ";" + contEditSocial3.value;
	}


	fetch(api + `/contact/update`, {
		method: 'PUT',
		headers: getHeaders(),
		body: JSON.stringify({
			"numCelu": contEditPhone.value,
			"email": contEditEmail.value,
			"direccion": contEditDir.value,
			"info": contEditDesc.value,
			"link1": contactLink1,
			"link2": contactLink2,
			"link3": contactLink3
		})
	}).then(response => {
		if(response.status === 500) throw new Error("Error interno del servidor");
		return response.json();
	}).then(data => {
		if(data.message) showErrorAlert(data.message);
		else {
			showSuccessAlert("Contacto actualizado exitosamente");
		}
	}).catch(error => {
		showErrorAlert(error);
		window.location.href = "/views/500.html";
	});
});



btnLogout.addEventListener('click', function () {
	document.cookie = "jwt=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/";
	window.location.href = "/index.html";
});



function setUserList(){
    fetch(api + '/user', {
        method: 'GET',
        headers: getHeaders()
    }).then(response => {
        if(response.status === 500) throw new Error("Error interno del servidor");
        return response.json();
    }).then(data => {
        if(data.message) showErrorAlert(data.message);
        else {
            const elemUl = document.getElementById("user__list_ul");
            for(let i = 0, l = data.length; i < l; i++){
                const myElem = document.createElement("li");
                myElem.textContent = data[i].nombre;
                elemUl.appendChild(myElem);
            }
        }
    }).catch(error => {
        showErrorAlert(error);
        window.location.href = "/views/500.html";
    });
}

function setProductList(){
    fetch(api + '/product', {
        method: 'GET',
        headers: getHeaders()
    }).then(response => {
        if(response.status === 500) throw new Error("Error interno del servidor");
        return response.json();
    }).then(data => {
        if(data.message) showErrorAlert(data.message);
        else {
            const elemUl = document.getElementById("product__list_ul");
            for(let i = 0, l = data.length; i < l; i++){
                const myElem = document.createElement("li");
                myElem.textContent = data[i].nombre;
                elemUl.appendChild(myElem);
            }
        }
    }).catch(error => {
        showErrorAlert(error);
        window.location.href = "/views/500.html";
    });
}

function setCategoryList(){
    fetch(api + '/category', {
        method: 'GET',
        headers: getHeaders()
    }).then(response => {
        if(response.status === 500) throw new Error("Error interno del servidor");
        return response.json();
    }).then(data => {
        if(data.message) showErrorAlert(data.message);
        else {
            const elemUl = document.getElementById("category__list_ul");
            for(let i = 0, l = data.length; i < l; i++){
                const myElem = document.createElement("li");
                myElem.textContent = data[i].nombre;
                elemUl.appendChild(myElem);
            }
        }
    }).catch(error => {
        showErrorAlert(error);
        window.location.href = "/views/500.html";
    });
}



function checkPrivileges(){
	const myHeaders = getHeaders();

	fetch(api + '/auth/checkrole', {
		method: 'GET',
		headers: myHeaders
	}).then(response => response.json())
	.then(data => {
		if(data.message) showErrorAlert(data.message);
		else {
			if(data < 2){
				window.location.href = "/views/403.html";
			} else {
				document.body.style.display = "block";
				setEditContact();
				setProductCategory();
				setProductList();
				setCategoryList();
				if(data === 3){
					document.getElementById("crud__select_user").style.display = "inline-block";
					setUserList();
				}
			}
		}
	})
	.catch(error => {
	  showErrorAlert(error);
	  window.location.href = "/views/500.html";
	});
}


prodMinInput.addEventListener('click', function (){
	setProductCategory();
});
prodMayInput.addEventListener('click', function (){
	setProductCategory();
});

document.addEventListener('DOMContentLoaded', function() {
	checkPrivileges();
});
