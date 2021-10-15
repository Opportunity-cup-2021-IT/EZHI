import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Config} from "../config";
import {Job} from "../types";
import * as moment from "moment";

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css'],
})
export class MenuComponent implements OnInit {

  @Input() job: Job | undefined;
  days: number | undefined;
  calc: boolean = false;
  cost: number | undefined;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
  }

  onCalc() {
    this.calc = true;
    this.http.get<number>(`${Config.URL}/job/${this.job?.id}/calc?days=${this.days}`)
      .subscribe((data: number) => {
        this.cost = data
        this.calc = false;
      }, error => {
        this.calc = false;
      });
  }

  formatDate(date: number): string {
    return moment.unix(date / 1000).format("YYYY-MM-DD HH:mm")
  }
}
