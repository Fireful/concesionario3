import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMoto } from 'app/shared/model/moto.model';
import { MotoService } from './moto.service';

@Component({
  templateUrl: './moto-delete-dialog.component.html'
})
export class MotoDeleteDialogComponent {
  moto?: IMoto;

  constructor(protected motoService: MotoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.motoService.delete(id).subscribe(() => {
      this.eventManager.broadcast('motoListModification');
      this.activeModal.close();
    });
  }
}
