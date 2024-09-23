import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Link } from 'react-router-dom';
import { NavItem, NavLink } from 'reactstrap';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/problem">
      <Translate contentKey="global.menu.entities.problem" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/test-case">
      <Translate contentKey="global.menu.entities.testCase" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/submission">
      <Translate contentKey="global.menu.entities.submission" />
    </MenuItem>
    {/* <MenuItem icon="asterisk" to="/submission-test-case-result">
      <Translate contentKey="global.menu.entities.submissionTestCaseResult" />
    </MenuItem> */}
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);

export const ProblemMenu = () => (
  <NavItem>
    <NavLink tag={Link} to="/problem" className="d-flex align-items-center">
      <FontAwesomeIcon icon="asterisk" />
      <span>
        <Translate contentKey="global.menu.entities.problem" />
      </span>
    </NavLink>
  </NavItem>
);
export const SubmissionMenu = () => (
  <NavItem>
    <NavLink tag={Link} to="/submission" className="d-flex align-items-center">
      <FontAwesomeIcon icon="asterisk" />
      <span>
        <Translate contentKey="global.menu.entities.submission" />
      </span>
    </NavLink>
  </NavItem>
);
export const TestCaseMenu = () => (
  <NavItem>
    <NavLink tag={Link} to="/test-case" className="d-flex align-items-center">
      <FontAwesomeIcon icon="asterisk" />
      <span>
        <Translate contentKey="global.menu.entities.testCase" />
      </span>
    </NavLink>
  </NavItem>
);
