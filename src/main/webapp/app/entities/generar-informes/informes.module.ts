import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Concesionario3SharedModule } from 'app/shared/shared.module';
import { InformesComponent } from './informes.component';
import { informesRoute } from './informes.route';

@NgModule({
  imports: [Concesionario3SharedModule, RouterModule.forChild(informesRoute)],
  declarations: [InformesComponent]
})
export class InformesModule {}
