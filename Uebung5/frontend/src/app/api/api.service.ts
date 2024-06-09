import {Injectable} from '@angular/core';
import axios from "axios";
import {OptionalEquipment} from "../optionalEquipment";

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private static readonly URL_ANALYSIS_STARTER: string = "http://localhost:8081"

  constructor() {

  }

  static startAnalysis(optionalEquipment: OptionalEquipment[]) {
    const json: { [key: string]: boolean } = {};

    optionalEquipment.forEach((equipment) => {
      json[equipment.key] = equipment.selected;
    });

    return axios.post(this.URL_ANALYSIS_STARTER + "/analyse", json, {})
  }
}
