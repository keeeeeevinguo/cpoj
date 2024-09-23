import dayjs from 'dayjs';
import { ITestCase } from 'app/shared/model/test-case.model';

export interface IProblem {
  id?: number;
  name?: string;
  title?: string;
  description?: string;
  createdBy?: string;
  createdDate?: string | null;
  lastModifiedBy?: string;
  lastModifiedDate?: string | null;
  testCases?: ITestCase[] | null;
}

export const defaultValue: Readonly<IProblem> = {};
