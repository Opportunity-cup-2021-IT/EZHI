import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Config} from "./config";
import {animate, style, transition, trigger} from "@angular/animations";
import {Job, Page} from "./types";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  animations: [
    trigger('fade', [
      transition(':enter', [
        style({opacity: 0}),
        animate(100, style({opacity: 1}))
      ]),
      transition(':leave', [
        style({opacity: 1}),
        animate(100, style({opacity: 0}))
      ])
    ])
  ]
})
export class AppComponent implements OnInit {

  data: Page<Job> | undefined;
  job: Job | undefined;
  jobMenuHide: boolean = false;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.requestPage(1);
  }

  requestPage(page: number) {
    this.http.get<Page<Job>>(`${Config.URL}/job/list?page=${page}&size=36`)
      .subscribe((data: Page<Job>) => this.data = data);
  }

  onClickJob(job: Job) {
    this.job = job;
    this.jobMenuHide = false;
  }

  onResetJob() {
    this.job = undefined;
    this.jobMenuHide = true;
    setTimeout(() => {
      this.jobMenuHide = false;
    }, 200)
  }
}
