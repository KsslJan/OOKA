import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {FormsModule} from "@angular/forms";

interface OptionalEquipment {
  id: string;
  name: string;
  value: boolean;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
  optionalEquipments: OptionalEquipment[] = [
    {id: "1", name: "oil", value: false},
    {id: "2", name: "fuel", value: false},
    {id: "3", name: "transmission", value: false},];

  toggleSelection(optionalEquipment: OptionalEquipment) {
    optionalEquipment.value = !optionalEquipment.value
  }
}
