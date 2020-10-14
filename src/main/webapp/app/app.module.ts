import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { Concesionario3SharedModule } from 'app/shared/shared.module';
import { Concesionario3CoreModule } from 'app/core/core.module';
import { Concesionario3AppRoutingModule } from './app-routing.module';
import { Concesionario3HomeModule } from './home/home.module';
import { Concesionario3EntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    Concesionario3SharedModule,
    Concesionario3CoreModule,
    Concesionario3HomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    Concesionario3EntityModule,
    Concesionario3AppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent]
})
export class Concesionario3AppModule {}
