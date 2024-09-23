import dayjs from 'dayjs';
import { IProblem } from 'app/shared/model/problem.model';

export interface ITestCase {
  id?: number;
  name?: string;
  timeLimitInMS?: number;
  memoryLimitInMB?: number;
  weightPercentage?: number;
  inputData?: string;
  outputData?: string;
  createdBy?: string;
  createdDate?: string | null;
  lastModifiedBy?: string;
  lastModifiedDate?: string | null;
  problem?: IProblem | null;
}

export const defaultValue: Readonly<ITestCase> = {};
