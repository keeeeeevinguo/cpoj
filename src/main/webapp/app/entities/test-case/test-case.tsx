import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { byteSize, Translate, TextFormat, getSortState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './test-case.reducer';
import { ITestCase } from 'app/shared/model/test-case.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TestCase = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );

  const testCaseList = useAppSelector(state => state.testCase.entities);
  const loading = useAppSelector(state => state.testCase.loading);
  const totalItems = useAppSelector(state => state.testCase.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
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
      <h2 id="test-case-heading" data-cy="TestCaseHeading">
        <Translate contentKey="cpojApp.testCase.home.title">Test Cases</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="cpojApp.testCase.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="cpojApp.testCase.home.createLabel">Create new Test Case</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {testCaseList && testCaseList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th />
                <th>
                  <Translate contentKey="cpojApp.testCase.problem">Problem</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                {/* <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="cpojApp.testCase.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                </th> */}
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="cpojApp.testCase.name">Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('timeLimitInMS')}>
                  <Translate contentKey="cpojApp.testCase.timeLimitInMS">Time Limit In MS</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('memoryLimitInMB')}>
                  <Translate contentKey="cpojApp.testCase.memoryLimitInMB">Memory Limit In MB</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('weightPercentage')}>
                  <Translate contentKey="cpojApp.testCase.weightPercentage">Weight Percentage</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('inputData')}>
                  <Translate contentKey="cpojApp.testCase.inputData">Input Data</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('outputData')}>
                  <Translate contentKey="cpojApp.testCase.outputData">Output Data</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="cpojApp.testCase.createdBy">Created By</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="cpojApp.testCase.createdDate">Created Date</Translate> <FontAwesomeIcon icon="sort" />
                </th>
              </tr>
            </thead>
            <tbody>
              {testCaseList.map((testCase, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${testCase.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${testCase.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${testCase.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                  <td>{testCase.problem ? <Link to={`problem/${testCase.problem.id}`}>{testCase.problem.name}</Link> : ''}</td>
                  {/* <td>
                    <Button tag={Link} to={`${match.url}/${testCase.id}`} color="link" size="sm">
                      {testCase.id}
                    </Button>
                  </td> */}
                  <td>{testCase.name}</td>
                  <td>{testCase.timeLimitInMS}</td>
                  <td>{testCase.memoryLimitInMB}</td>
                  <td>{testCase.weightPercentage}</td>
                  <td>{testCase.inputData}</td>
                  <td>{testCase.outputData}</td>
                  <td>{testCase.createdBy}</td>
                  <td>{testCase.createdDate ? <TextFormat type="date" value={testCase.createdDate} format={APP_DATE_FORMAT} /> : null}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="cpojApp.testCase.home.notFound">No Test Cases found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={testCaseList && testCaseList.length > 0 ? '' : 'd-none'}>
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

export default TestCase;
