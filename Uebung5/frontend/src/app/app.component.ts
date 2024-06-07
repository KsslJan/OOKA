import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {FormsModule} from "@angular/forms";
import {LayoutComponent} from "./layout/layout.component";
import {ChipComponent} from "./chip/chip.component";
import {Status} from "./chip/status";
import {NgForOf} from "@angular/common";
import {ButtonComponent} from "./button/button.component";
import {ExpansionPanelComponent} from "./expansion-panel/expansion-panel.component";
import {TopbarComponent} from "./topbar/topbar.component";

interface OptionalEquipment {
  id: string;
  name: string;
  value: boolean;
  status: Status;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FormsModule, LayoutComponent, ChipComponent, NgForOf, ButtonComponent, ExpansionPanelComponent, TopbarComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  showResult = true; //TODO set to false later

  title = 'frontend';
  optionalEquipments: OptionalEquipment[] = [
    {id: "1", name: "oil", value: false, status: Status.Unselected},
    {id: "2", name: "fuel", value: false, status: Status.Selected},
    {id: "2", name: "fuel", value: false, status: Status.Running},
    {id: "3", name: "transmission", value: true, status: Status.Success},
    {id: "4", name: "engine", value: true, status: Status.Failed},
  ];

  toggleSelection(optionalEquipment: OptionalEquipment) {
    optionalEquipment.value = !optionalEquipment.value
  }

  startAnalysis() {
    this.showResult = !this.showResult;
  }

  protected readonly Status = Status;
  protected readonly alert = alert;
}
