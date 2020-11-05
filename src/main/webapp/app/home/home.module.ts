import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Concesionario3SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { HomeResetVentasComponent } from './home-reset-ventas.component';

@NgModule({
  imports: [Concesionario3SharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent, HomeResetVentasComponent],
  entryComponents: [HomeResetVentasComponent]
})
export class Concesionario3HomeModule {}
