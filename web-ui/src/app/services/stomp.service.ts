import {Injectable, EventEmitter} from '@angular/core';

var SockJS = require('sockjs');
var Stomp = require('stompjs');

@Injectable()
export class StompService {
    stompClient: any;

    send() {
        this.stompClient.send('/app/hello', {},      JSON.stringify({ 'name': 'xxx' }));
    }

    connect() {
        var that = this;
        var socket = new SockJS('/hello');
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({}, (frame: any) => {
            console.log('Connected: >>>>>>>>>>>> ' + frame);
            that.subscribe();
        }, (err: any) => {
            console.log('err', err);
        });
    }

    subscribe() {
        this.stompClient.subscribe('/topic/greetings', (greeting: any) => {
            console.log(greeting);
        });
    }

}

// Angular 2
// =========================== STOMP ====================================
//
// this.loadPromise = promise.then(response => {
//     this.updateResponseForDashboardDateFilter(response, cardUpdateModel);
//     return this.handleLoadResponse(params, response);
// }).catch((error) => {
//     console.error('error loading:', cardId, error);
//     this.DataProvider.clearCache('card/metrics/' + cardId, params, 'GET');
//     this.forEach(metric => metric.clear());
//     this.error = true;
//     this.loadPromise = null;
// }).finally(() => {
//     this.loaded = true;
//     this.trigger('loaded');
// });
//
// return this.loadPromise;
// }
//
// updateResponseForDashboardDateFilter(response, cardUpdateModel) {
//     if (cardUpdateModel) {
//         let dateDashboardFilterModel = cardUpdateModel.find(item => item.type === 'date');
//         let representationType = response.type;
//         if (dateDashboardFilterModel && representationType && representationType !== 'map') {
//             this.card.types.type = this.card.types.subType = representationType;
//         }
//     }
// }
//
//
// ============================ npm deps ============================================
//
//
// "sockjs": "0.3.18",
//     "stompjs": "^2.3.3",
//
//
//     <script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.js"></script>
//     <script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.1/sockjs.js"></script>
// ============================ row js ============================================
// (function () {
//     var stompClient = null, WSService;
//
//     WSService = {
//         disconnect: function disconnect() {
//             if (stompClient != null) {
//                 stompClient.disconnect();
//             }
//             console.log("Disconnected");
//         },
//         sendName: function sendName() {
//             stompClient.send("/app/hello", {}, JSON.stringify({'name': 'xxXxx'}));
//         },
//         connect: function connect() {
//             var socket = new SockJS('/ws');
//             stompClient = Stomp.over(socket);
//             stompClient.connect({}, function (frame) {
//                 console.log('Connected >>>>>>>>>>>>>>>>>>>>>>>>>: ' + frame);
//                 stompClient.subscribe('/topic/greetings', function (greeting) {
//                     console.log(greeting);
//                 });
//             });
//         }
//     };
//
//     WSService.connect();
//
//     if (typeof exports !== "undefined" && exports !== null) {
//         exports.WSService = WSService;
//     }
//
//     if (typeof window !== "undefined" && window !== null) {
//         WSService.setInterval = function (interval, f) {
//             return window.setInterval(f, interval);
//         };
//         WSService.clearInterval = function (id) {
//             return window.clearInterval(id);
//         };
//         window.WSService = WSService;
//     } else if (!exports) {
//         self.WSService = WSService;
//     }
//
//     if (typeof window !== "undefined" && window !== null) {
//         WSService.setInterval = function (interval, f) {
//             return window.setInterval(f, interval);
//         };
//         WSService.clearInterval = function (id) {
//             return window.clearInterval(id);
//         };
//         window.WSService = WSService;
//     } else if (!exports) {
//         self.WSService = WSService;
//     }
//
// }).call(this);