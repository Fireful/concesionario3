import { Routes } from '@angular/router';

import { InformesComponent } from './informes.component';

export const informesRoute: Routes = [
  {
    path: '',
    component: InformesComponent,
    data: {
      pageTitle: 'concesionario3App.informes.home.title'
    }
  }
];
