import dayjs from 'dayjs';
import { ISubmissionTestCaseResult } from 'app/shared/model/submission-test-case-result.model';
import { IProblem } from 'app/shared/model/problem.model';
import { ProgrammingLanguage } from 'app/shared/model/enumerations/programming-language.model';
import { SubmissionResultStatus } from 'app/shared/model/enumerations/submission-result-status.model';
import { SubmissionResultMessage } from 'app/shared/model/enumerations/submission-result-message.model';

export interface ISubmission {
  id?: number;
  name?: string;
  programmingLanguage?: ProgrammingLanguage | null;
  code?: string;
  overallResultStatus?: SubmissionResultStatus;
  overallResultMessage?: SubmissionResultMessage;
  overallResultMessageDetail?: string | null;
  overallResultTries?: number;
  overallResultScorePercentage?: number;
  createdBy?: string;
  createdDate?: string | null;
  lastModifiedBy?: string;
  lastModifiedDate?: string | null;
  submissionTestCaseResults?: ISubmissionTestCaseResult[] | null;
  problem?: IProblem | null;
}

export const defaultValue: Readonly<ISubmission> = {};
