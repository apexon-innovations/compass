import React, { PureComponent } from 'react'
import { NotificationManager } from 'react-notifications'
import PropTypes from 'prop-types'
import { ReactSVG } from 'react-svg'
import Scrollbars from 'react-custom-scrollbars'
import { Navbar, Nav, DropdownButton, Dropdown } from 'react-bootstrap'
import { IMG_DASHBOARD_SWITCH, USER_DUMMY } from '../../const/imgConst.js'
import { clearLocalStorage, isUserLogin } from '../../utils/loginFunction'
import { postApiCall, getApiCall } from '../../utils/apiCall'
import { setInitialBoardAndRepoDataWithDispatchEvt } from '../../utils/projectDataStoreFunction'
import style from './Header.module.scss'

const baseUrl = process.env.REACT_APP_API_END_POINT
let tempSearchBoxValue = ''

class Header extends PureComponent {
  constructor(props) {
    super(props)
    this.state = {
      searchTerm: '',
      projectList: [],
      filterData: [],
      isLoading: false,
      isDisplaySearchResult: false,
    }
    this.wrapperRef = React.createRef()
    this.handleClick = this.handleClick.bind(this)
  }

  onSelect(e) {
    if (e === '4') {
      postApiCall(`${baseUrl}/user-service/user/logout`)
        .then(responseData => {
          if (responseData.success) {
            clearLocalStorage()
            NotificationManager.success('Logout successfully..!', '', 5000)
            window.location = '/login'
          } else if (responseData.data.errorCode === 401) {
            clearLocalStorage()
            NotificationManager.success('Logout successfully..!', '', 5000)
            window.location = '/login'
          } else {
            NotificationManager.error(responseData.data.message, '', 5000)
          }
        })
        .catch(() => {
          NotificationManager.error('APIs end point not working', '', 5000)
        })
    }
  }

  onSearchProject(searchBoxValue) {
    const { projectList, isLoading } = this.state

    tempSearchBoxValue = searchBoxValue
    if ((!projectList || projectList.length < 1) && !isLoading) {
      const apiEndPointUrl = `${baseUrl}/psr-service/project/all`
      this.setState({
        isLoading: true,
        isDisplaySearchResult: true,
      })
      getApiCall(apiEndPointUrl).then(responseData => {
        if (responseData.success) {
          this.setState(
            {
              projectList: responseData.data.data,
              isLoading: false,
            },
            () => {
              this.filterDataResult(tempSearchBoxValue)
            },
          )
        } else {
          this.setState({
            isLoading: false,
            filteredData: 'Something Went Wrong. Please try again !!',
          })
        }
      })
    }

    if (!isLoading) {
      this.filterDataResult(tempSearchBoxValue)
    } else {
      this.setState({
        filteredData: [],
      })
    }
  }

  filterDataResult(searchBoxValue) {
    const tempFilterData = this.state.projectList.filter(element => {
      return (
        element.name.toLowerCase().includes(searchBoxValue.toLowerCase().trim()) ||
        element.initials.toLowerCase().includes(searchBoxValue.toLowerCase().trim())
      )
    })

    this.setState({
      filteredData:
        tempFilterData && tempFilterData.length > 0
          ? tempFilterData
          : searchBoxValue
          ? 'No Data Found ..!!'
          : '',
      isDisplaySearchResult: true,
    })
  }

  /* eslint-disable */
  handleClick(e) {
    if (this.wrapperRef && this.wrapperRef.current && !this.wrapperRef.current.contains(e.target)) {
      this.setState({
        isDisplaySearchResult: false,
      })
    }
  }

  componentDidMount() {
    document.addEventListener('mousedown', this.handleClick, false)
  }
  componentWillUnmount() {
    document.removeEventListener('mousedown', this.handleClick, false)
  }

  /* eslint-enable */

  render() {
    const isLogin = isUserLogin()
    const { isLoading, filteredData, isDisplaySearchResult } = this.state

    return (
      <div className={style.header}>
        <Navbar expand="lg" bg="transparent">
          <Navbar.Brand href={isLogin ? '/home' : '/'}>
            <img className="d-inline-block align-top" src={IMG_DASHBOARD_SWITCH} title="" alt="" />
          </Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          {isLogin ? (
            <Navbar.Collapse id="basic-navbar-nav">
              <Nav className="ml-auto">
                {this.props.isSearchBox ? (
                  <div className={style.searchBox} ref={this.wrapperRef}>
                    <div id="userNameControl" className={style.inputBox}>
                      <input
                        type="text"
                        placeholder="Search Project"
                        onChange={e => {
                          this.onSearchProject(e.target.value)
                        }}
                      />
                      <div className={style.searchResultBox}>
                        {isDisplaySearchResult && !isLoading ? (
                          <Scrollbars autoHeight autoHeightMax={200}>
                            <ul>
                              {filteredData && Array.isArray(filteredData) ? (
                                <React.Fragment>
                                  {filteredData.map((item, index) => {
                                    return (
                                      <li
                                        key={index}
                                        onClick={() => {
                                          setInitialBoardAndRepoDataWithDispatchEvt(item)
                                          window.location = `${item.id}`
                                        }}
                                      >
                                        {item.name}
                                      </li>
                                    )
                                  })}
                                </React.Fragment>
                              ) : (
                                <li>{filteredData}</li>
                              )}
                            </ul>
                          </Scrollbars>
                        ) : (
                          ''
                        )}
                        {isLoading ? (
                          <ul className={style.loader}>
                            <li>
                              {' '}
                              <ReactSVG src={`${IMG_DASHBOARD_SWITCH}`} />
                            </li>
                          </ul>
                        ) : (
                          ''
                        )}
                      </div>
                    </div>
                  </div>
                ) : (
                  ''
                )}
                <div className={[style.transparentDropdownBox, 'transparentDropdownBox'].join(' ')}>
                  <DropdownButton
                    className={style.headerDropdown}
                    alignRight
                    title={
                      <div className={style.user_icon}>
                        <img src={USER_DUMMY} title="" alt="profile" />
                      </div>
                    }
                    onSelect={this.onSelect}
                    id="dropdown-menu-align-right"
                  >
                    <Dropdown.Item eventKey="4">Sign Out </Dropdown.Item>
                  </DropdownButton>
                </div>
              </Nav>
            </Navbar.Collapse>
          ) : (
            ''
          )}
        </Navbar>
      </div>
    )
  }
}

Header.propTypes = {
  isSearchBox: PropTypes.any,
}

export default Header
