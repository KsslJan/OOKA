import {Status} from "./chip/status";

export abstract class OptionalEquipment {
  abstract key: string;
  abstract name: string;
  abstract selected: boolean;
  status?: Status = Status.Unselected;
}
