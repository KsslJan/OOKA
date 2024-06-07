import {Component, Input} from '@angular/core';
import {Status} from "./status";

@Component({
  selector: 'app-chip',
  standalone: true,
  imports: [],
  templateUrl: './chip.component.html',
  styleUrl: './chip.component.css'
})

export class ChipComponent {
  @Input() status: Status = Status.Unselected;

  getColor() {
    switch (this.status) {
      case Status.Unselected:
        return '#eee';
      case Status.Selected:
        return '#bbb';
      case Status.Running:
        return '#ffda68';
      case Status.Failed:
        return '#fb1626';
      case Status.Success:
        return '#48a633';
      default:
        return 'white';
    }
  }
}
