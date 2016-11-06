import {Injectable, EventEmitter} from '@angular/core';
import * as Rx from 'rxjs/Rx';



@Injectable()
export class WebSocketService {
    private subject: Rx.Subject<MessageEvent>;

    private ws: WebSocket;

    public connect(url: string): Rx.Subject<MessageEvent> {
        if (!this.subject) {
            this.subject = this.create(url);
        }
        return this.subject;
    }

    public send(data: any) {
        this.ws.send(data);
    }

    private create(url: string): Rx.Subject<MessageEvent> {
        this.ws = new WebSocket(url + '?access_token=' + localStorage.getItem('access_token'));
        let observable = Rx.Observable.create(
            (obs: Rx.Observer<MessageEvent>) => {
                this.ws.onmessage = obs.next.bind(obs);
                this.ws.onerror = obs.error.bind(obs);
                this.ws.onclose = obs.complete.bind(obs);

                return this.ws.close.bind(this.ws);
            });

        let observer = {
            next: (data: Object) => {
                if (this.ws.readyState === WebSocket.OPEN) {
                    this.ws.send(JSON.stringify(data));
                }
            }
        };

        return Rx.Subject.create(observer, observable);
    }
}