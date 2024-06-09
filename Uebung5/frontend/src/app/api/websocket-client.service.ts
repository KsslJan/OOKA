import {Injectable} from '@angular/core';
import {Status} from "../chip/status";
import {Client} from "@stomp/stompjs";

@Injectable({
  providedIn: 'root'
})

export class WebsocketClientService {
  stompClient = new Client({
    logRawCommunication: true,
    brokerURL: 'http://localhost:8081/ws'
  });

  constructor() {
    interface WsResult { // TODO
      id: string,
      result: Status
    }

    // const webSocket = new WebSocket('http://localhost:8080/ws', "http");
    // this.stompClient = Stomp.over(webSocket)

    this.stompClient.onConnect = () => {
      console.log("connecting over websocket to the server");
      this.stompClient.subscribe('/results/analysisResult', (result) => {
        console.log("result came back", result);
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
