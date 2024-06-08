import {Injectable} from '@angular/core';
import {Stomp} from "@stomp/stompjs";
import {Status} from "../chip/status";

@Injectable({
  providedIn: 'root'
})

export class WebsocketClientService {
  stompClient = Stomp.client('ws://localhost:8080/gs-guide-websocket');

  constructor() {
    interface WsResult { // TODO
      id: string,
      result: Status
    }

    this.stompClient.onConnect = () => {
      console.log("connecting over websocket to the server");
      this.stompClient.subscribe('/result', (result) => {
        // showGreeting(JSON.parse(result.body).content);
        // TODO
      });
    };

    this.stompClient.onWebSocketError = (error) => {
      console.error('Error with websocket', error);
    };

    this.stompClient.onStompError = (frame) => {
      console.error('Broker reported error: ' + frame.headers['message']);
      console.error('Additional details: ' + frame.body);
    };
  }

  connect() {
    this.stompClient.activate();
  }

  disconnect() {
    this.stompClient.deactivate();
    console.log("Disconnected");
  }
}
