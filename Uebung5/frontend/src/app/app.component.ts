import {Component} from '@angular/core';
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

interface OptionalEquipment {
  id: string;
  name: string;
  selected: boolean;
  status: Status;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FormsModule, LayoutComponent, ChipComponent, NgForOf, ButtonComponent, ExpansionPanelComponent, TopbarComponent, NgIf],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  showResult = false;

  title = 'frontend';
  optionalEquipments: OptionalEquipment[] = [
    {id: "1", name: "exhaust", selected: false, status: Status.Selected},
    {id: "2", name: "fluids", selected: false, status: Status.Selected},
    {id: "3", name: "gearbox options", selected: true, status: Status.Selected},
    {id: "4", name: "starting system", selected: true, status: Status.Selected},
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

  startAnalysis() {
    this.showResult = !this.showResult;
    ApiService.startAnalysis();
  }

  ngOnInit(): void {
    this.wsClientService.connect()

    for (let oe of this.optionalEquipments) {
      AppComponent.updateStatus(oe);
    }
  }

  protected readonly Status = Status;
  protected readonly alert = alert;
}
