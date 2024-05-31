const api = "http://localhost:8080/api/v1";

function setContactFromDB() {
    fetch(api + '/contact', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => response.json())
    .then(data => {
		if(data.message){
			elemAlertError = document.getElementById('alert_error');
			elemAlertError.innerHTML = data.message;
			elemAlertError.style.display = "block";
			setTimeout(function() {
				elemAlertError.style.display = "none";
			}, 5000);
            console.error(data.message);
		} else {
            const contactContainer = document.getElementById("footer__contact");

            const elemSpan = document.createElement('span');

            elemSpan.textContent = data.numCelu;
            contactContainer.appendChild(elemSpan);

            const elemSpanMail = document.createElement('span');
            elemSpanMail.textContent = data.email;
            contactContainer.appendChild(elemSpanMail);

            const elemSpanDir = document.createElement('span');
            elemSpanDir.textContent = data.direccion;
            contactContainer.appendChild(elemSpanDir);

            const elemUl = document.getElementById("footer__links_container");

            const elemInfo = document.getElementById('footer__about');
            const elemP = document.createElement('p');
            elemP.textContent = data.info;
            elemInfo.appendChild(elemP);

            for(let i = 1; i <= 3; i++){
                const elemAnchor = document.createElement('a');
                const elemLi = document.createElement('li');

                let linkNumber = "link" + i;
                const link = data[linkNumber];
                let link_text, link_url = '';

                for(let j = 0; j < link.length; j++) {
                    if(link[j] === ";"){
                        link_text = link.substring(0, j);
                        link_url = link.substring(j + 1, link.length);
                        break;
                    }
                }

                elemAnchor.textContent = link_text;
                elemAnchor.setAttribute('href', link_url);
                elemAnchor.setAttribute('target', "_blank");

                elemLi.appendChild(elemAnchor);
                elemUl.appendChild(elemLi);
            }
        }
    })
    .catch(error => {
        console.error('Error:', error);
        window.location.href = "/views/500.html";
    });
}

document.addEventListener('DOMContentLoaded', function() {
    setContactFromDB();
});
