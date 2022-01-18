import { Component, OnInit, VERSION } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbCarouselConfig } from '@ng-bootstrap/ng-bootstrap';
import { LoginService } from 'app/login/login.service';

@Component({
  selector: 'jhi-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.scss'],
  providers: [NgbCarouselConfig],
})
export class MenuComponent implements OnInit {
  name = 'Angular ' + VERSION.major;
  rotateSetting: boolean | undefined;
  rotateProfile: boolean | undefined;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected loginService: LoginService,
    protected router: Router,
    config: NgbCarouselConfig
  ) {
    // customize default values of carousels used by this component tree
    config.interval = 2000;
    config.keyboard = true;
    config.pauseOnHover = true;
  }

  ngOnInit(): void {
    console.log('heloo');
  }

  previousState(): void {
    window.history.back();
  }
  try(drawer: any, event: any) {
    event.stopPropagation();
    drawer.toggle();
  }
  closeSidebar(drawer: any) {
    if (drawer._animationState === 'open') {
      console.log('close now');
      drawer.toggle();
    }
  }
  openSetting() {
    this.rotateSetting = !this.rotateSetting;
  }
  openProfile() {
    this.rotateProfile = !this.rotateProfile;
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['/login']);
    window.location.reload();
  }
}
