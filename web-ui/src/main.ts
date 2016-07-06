import { bootstrap } from '@angular/platform-browser-dynamic';
import { enableProdMode } from '@angular/core';
import { AppComponent } from './app/app.component';
if (process.env.ENV === 'production') {
    enableProdMode();
}


function loadUsersAPI() {
    getJSON('api', function (api) {
        console.log(api);
        bootstrap(AppComponent, []);
    }, function (error) {
        console.error(error);
    });
}

function getJSON(url, success, error) {
    'use strict';
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                success(JSON.parse(xhr.responseText));
            } else {
                error(xhr.responseText);
            }
        }
    };
    xhr.open('GET', url);
    xhr.send();
}

loadUsersAPI();
