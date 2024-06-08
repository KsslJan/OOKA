import {Injectable} from '@angular/core';
import axios from "axios";

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private readonly URL_ANALYSIS_STARTER: string = "http://localhost:8081"

  constructor() {

  }

  startAnalysis() {
    return axios.post(this.URL_ANALYSIS_STARTER + "/analyse", {}, {})
  }
}
