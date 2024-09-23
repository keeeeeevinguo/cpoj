import dayjs from 'dayjs';
import { ISubmission } from 'app/shared/model/submission.model';
import { TestCaseResultMessage } from 'app/shared/model/enumerations/test-case-result-message.model';

export interface ISubmissionTestCaseResult {
  id?: number;
  name?: string;
  resultMessage?: TestCaseResultMessage;
  resultMessageDetail?: string | null;
  elapsedTimeInMS?: number;
  createdBy?: string;
  createdDate?: string | null;
  lastModifiedBy?: string;
  lastModifiedDate?: string | null;
  submission?: ISubmission | null;
}

export const defaultValue: Readonly<ISubmissionTestCaseResult> = {};
