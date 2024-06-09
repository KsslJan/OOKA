import {Injectable} from '@angular/core';
import axios from "axios";

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private static readonly URL_ANALYSIS_STARTER: string = "http://localhost:8081"

  constructor() {

  }

  static startAnalysis() {
    let json = {
      startingSystem: false,
      auxPTO: false,
      oilSystem: false,
      fuelSystem: false,
      coolingSystem: false,
      exhaustSystem: false,
      mountingSystem: false,
      engineManagementSystem: true,
      monitoringControlSystem: false,
      powerTransmission: false,
      gearBoxOptions: false,
    }
    return axios.post(this.URL_ANALYSIS_STARTER + "/analyse", json, {})
  }
}
