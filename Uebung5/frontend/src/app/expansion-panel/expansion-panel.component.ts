import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-expansion-panel',
  standalone: true,
  imports: [],
  templateUrl: './expansion-panel.component.html',
  styleUrl: './expansion-panel.component.css'
})
export class ExpansionPanelComponent {
  @Input() open: boolean = true;
  @Input() summary: string = "";
}
