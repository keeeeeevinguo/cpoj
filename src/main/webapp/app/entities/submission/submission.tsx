import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './submission.reducer';
import { ISubmission } from 'app/shared/model/submission.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

export const Submission = (props: RouteComponentProps<{ url: string }>) => {
  const account = useAppSelector(state => state.authentication.account);
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));

  const dispatch = useAppDispatch();

  const initialPaginationState = overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  initialPaginationState.sort = "createdDate";
  initialPaginationState.order = "desc";
  const [paginationState, setPaginationState] = useState(
    initialPaginationState
  );

  const submissionList = useAppSelector(state => state.submission.entities);
  const loading = useAppSelector(state => state.submission.loading);
  const totalItems = useAppSelector(state => state.submission.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
        username: account?.login && !isAdmin ? account?.login : null
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (props.location.search !== endURL) {
      props.history.push(`${props.location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(props.location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [props.location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const { match } = props;

  return (
    <div>
      <h2 id="submission-heading" data-cy="SubmissionHeading">
        <Translate contentKey="cpojApp.submission.home.title">Submissions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="cpojApp.submission.home.refreshListLabel">Refresh List</Translate>
          </Button>
        </div>
      </h2>
      <div className="table-responsive">
        {submissionList && submissionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th />
                <th>
                  <Translate contentKey="cpojApp.submission.problem">Problem</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('programmingLanguage')}>
                  <Translate contentKey="cpojApp.submission.programmingLanguage">Programming Language</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('overallResultStatus')}>
                  <Translate contentKey="cpojApp.submission.overallResultStatus">Overall Result Status</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('overallResultMessage')}>
                  <Translate contentKey="cpojApp.submission.overallResultMessage">Overall Result Message</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('overallResultScorePercentage')}>
                  <Translate contentKey="cpojApp.submission.overallResultScorePercentage">Overall Result Score Percentage</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="cpojApp.submission.createdBy">Created By</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="cpojApp.submission.createdDate">Created Date</Translate> <FontAwesomeIcon icon="sort" />
                </th>
              </tr>
            </thead>
            <tbody>
              {submissionList.map((submission, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${submission.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      {/* <Button
                        tag={Link}
                        to={`${match.url}/${submission.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button> */}
                      {isAdmin &&
                        <Button
                          tag={Link}
                          to={`${match.url}/${submission.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                          color="danger"
                          size="sm"
                          data-cy="entityDeleteButton"
                        >
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      }
                    </div>
                  </td>
                  <td>{submission.problem ? <Link to={`problem/${submission.problem.id}`}>{submission.problem.name}</Link> : ''}</td>
                  <td>
                    <Translate contentKey={`cpojApp.ProgrammingLanguage.${submission.programmingLanguage}`} />
                  </td>
                  <td>
                    <Translate contentKey={`cpojApp.SubmissionResultStatus.${submission.overallResultStatus}`} />
                  </td>
                  <td>
                    <Translate contentKey={`cpojApp.SubmissionResultMessage.${submission.overallResultMessage}`} />
                  </td>
                  <td>{submission.overallResultScorePercentage}</td>
                  <td>{submission.createdBy}</td>
                  <td>
                    {submission.createdDate ? <TextFormat type="date" value={submission.createdDate} format={APP_DATE_FORMAT} /> : null}
                  </td>

                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="cpojApp.submission.home.notFound">No Submissions found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={submissionList && submissionList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Submission;
