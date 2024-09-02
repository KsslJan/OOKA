import {Component, OnInit} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {FormsModule} from "@angular/forms";
import {LayoutComponent} from "./layout/layout.component";
import {ChipComponent} from "./chip/chip.component";
import {Status} from "./chip/status";
import {NgForOf, NgIf} from "@angular/common";
import {ButtonComponent} from "./button/button.component";
import {ExpansionPanelComponent} from "./expansion-panel/expansion-panel.component";
import {TopbarComponent} from "./topbar/topbar.component";
import {WebsocketClientService} from "./api/websocket-client.service";
import {ApiService} from "./api/api.service";
import {OptionalEquipment} from "./optionalEquipment";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FormsModule, LayoutComponent, ChipComponent, NgForOf, ButtonComponent, ExpansionPanelComponent, TopbarComponent, NgIf],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  showResult = false;

  title = 'frontend';
  optionalEquipments: OptionalEquipment[] = [
    {key: "startingSystem", name: "Starting system", selected: false},
    {key: "auxPTO", name: "Auxiliary PTO", selected: false},
    {key: "oilSystem", name: "Oil system", selected: false},
    {key: "fuelSystem", name: "Fuel system", selected: false},
    {key: "coolingSystem", name: "Cooling system", selected: false},
    {key: "exhaustSystem", name: "Exhaust system", selected: false},
    {key: "mountingSystem", name: "Mounting system", selected: false},
    {key: "engineManagementSystem", name: "Engine management system", selected: false},
    {key: "monitoringControlSystem", name: "Monitoring control system", selected: false},
    {key: "powerTransmission", name: "Power transmission", selected: false},
    {key: "gearBoxOptions", name: "Gearbox options", selected: false},
  ];

  constructor(private wsClientService: WebsocketClientService) {
  }

  toggleSelection(optionalEquipment: OptionalEquipment) {
    optionalEquipment.selected = !optionalEquipment.selected
    if (optionalEquipment.status === Status.Unselected || optionalEquipment.status === Status.Selected) {
      AppComponent.updateStatus(optionalEquipment);
    }
  }

  private static updateStatus(optionalEquipment: OptionalEquipment) {
    if (optionalEquipment.selected) {
      optionalEquipment.status = Status.Selected
    } else {
      optionalEquipment.status = Status.Unselected
    }
  }

  isAnalysisEnabled() {
    return this.optionalEquipments.some(value => value.status === Status.Selected);
  }

  startAnalysis() {
    this.showResult = true;
    this.optionalEquipments.forEach(value => {
      if (value.status === Status.Selected) {
        value.status = Status.Running;
      }
    })
    ApiService.startAnalysis(this.optionalEquipments);
  }

  ngOnInit(): void {
    this.wsClientService.connect()

    for (let oe of this.optionalEquipments) {
      AppComponent.updateStatus(oe);
    }

    this.wsClientService.optionalEquipmentChanged.subscribe(webSocketResult => {
      if (this.showResult && Status.Running) { // TODO not perfect, cause if too quickly restarted the result will be overwritten
        const index = this.optionalEquipments.findIndex((item) => item.key === webSocketResult.key);
        this.optionalEquipments[index].status = webSocketResult.value ? Status.Success : Status.Failed;
      }
    });
  }

  resetSelection() {
    this.optionalEquipments.forEach(optionalEquipment => {
      optionalEquipment.selected = false;
      optionalEquipment.status = Status.Unselected;
      this.showResult = false;
    });
  }

  protected readonly Status = Status;

  getFinalStatus() {
    if (this.optionalEquipments.some(value => value.status === Status.Running))
      return Status.Running;
    return this.optionalEquipments.some(value => value.status === Status.Failed) ? Status.Failed : Status.Success;
  }
}
