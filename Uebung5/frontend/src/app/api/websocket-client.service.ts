import {EventEmitter, Injectable} from '@angular/core';
import {Client} from "@stomp/stompjs";
import {WebSocketResult} from "./WebSocketResult";

@Injectable({
  providedIn: 'root'
})

export class WebsocketClientService {
  optionalEquipmentChanged = new EventEmitter<WebSocketResult>();

  updateOptionalEquipment(optionalEquipment: WebSocketResult) {
    this.optionalEquipmentChanged.emit(optionalEquipment);
  }

  stompClient = new Client({
    brokerURL: 'http://localhost:8081/ws'
  });

  constructor() {

    this.stompClient.onConnect = () => {
      console.log("connecting over websocket to the server");
      this.stompClient.subscribe('/results/analysisResult', (result) => {
        let websocketResult = JSON.parse(result.body) as unknown as WebSocketResult;
        console.log("result came back", websocketResult);
        this.updateOptionalEquipment(websocketResult)
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
