import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs/Rx';
import {WebSocketService } from './websocket.service';

// import './ws.service.js';

const CHAT_URL = 'ws://localhost:8065/ws';

export interface Message {
    author: string,
    message: string,
    newDate?: number
}

@Injectable()
export class WebSocketSubscriptionService {
    public messages: Subject<Message>;

    constructor(private wsService: WebSocketService) {
        this.messages = <Subject<Message>>wsService
            .connect(CHAT_URL)
            .map((response: MessageEvent): Message => {
                console.log(response);
                // let data = JSON.parse(response.data);
                return {
                    author: 'test',
                    message: response.data,
                    newDate : response.timeStamp
                }
            });
    }

}