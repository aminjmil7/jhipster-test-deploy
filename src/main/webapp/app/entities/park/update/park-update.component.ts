import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPark, Park } from '../park.model';
import { ParkService } from '../service/park.service';

@Component({
  selector: 'jhi-park-update',
  templateUrl: './park-update.component.html',
})
export class ParkUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    parkName: [],
    parkAddress: [],
    longtitude: [],
    latitude: [],
    verified: [],
  });

  constructor(protected parkService: ParkService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ park }) => {
      this.updateForm(park);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const park = this.createFromForm();
    if (park.id !== undefined) {
      this.subscribeToSaveResponse(this.parkService.update(park));
    } else {
      this.subscribeToSaveResponse(this.parkService.create(park));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPark>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(park: IPark): void {
    this.editForm.patchValue({
      id: park.id,
      parkName: park.parkName,
      parkAddress: park.parkAddress,
      longtitude: park.longtitude,
      latitude: park.latitude,
      verified: park.verified,
    });
  }

  protected createFromForm(): IPark {
    return {
      ...new Park(),
      id: this.editForm.get(['id'])!.value,
      parkName: this.editForm.get(['parkName'])!.value,
      parkAddress: this.editForm.get(['parkAddress'])!.value,
      longtitude: this.editForm.get(['longtitude'])!.value,
      latitude: this.editForm.get(['latitude'])!.value,
      verified: this.editForm.get(['verified'])!.value,
    };
  }
}
