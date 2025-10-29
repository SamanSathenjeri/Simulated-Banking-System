import { TestBed } from '@angular/core/testing';

import { EnvelopeService } from './envelope';

describe('Envelope', () => {
  let service: EnvelopeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EnvelopeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
