import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICoche } from 'app/shared/model/coche.model';
import { CocheService } from './coche.service';

@Component({
  templateUrl: './coche-delete-dialog.component.html'
})
export class CocheDeleteDialogComponent {
  coche?: ICoche;

  constructor(protected cocheService: CocheService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cocheService.delete(id).subscribe(() => {
      this.eventManager.broadcast('cocheListModification');
      this.activeModal.close();
    });
  }
}
