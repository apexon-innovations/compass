import React from 'react'
import PropTypes from 'prop-types'
import { ReactSVG } from 'react-svg'
import OAuth2Login from 'react-simple-oauth2-login'
import { NotificationManager } from 'react-notifications'
import { handleLoginSuccess } from './LoginFormFunction'
import { ReactComponent as EnterSVG } from 'images/enter_icon.svg'
import { SVG_LOADER } from 'reusable-components/dist/const/imgConst'
import { withRouter } from 'reusable-components/dist/components/withRouter/withRouter'
import './LoginForm.scss'

class LoginForm extends React.PureComponent {
  constructor(props) {
    super(props)
    this.state = {
      email: '',
      password: '',
      step: 1,
      isChecked: false,
      isLoading: false,
      userNameClass: '',
      userNameEnterClass: '',
      passwrdClass: 'bottom_out',
      subConsoleClass: 'bottom_out',
      userPasswordControl: '',
      userNameControl: '',
    }
  }

  handleSuccessResponse = data => {
    handleLoginSuccess(this.props, data)
  }

  onSuccess = ({ code }) => {
    let authorizationToken = btoa(
      `${process.env.REACT_APP_CLIENT_ID}:${process.env.REACT_APP_CLIENT_SECRET}`
    );

    const details = {
      grant_type: "authorization_code",
      code: code,
      redirect_uri: `${window.location.origin}/callback`,
      client_id: "compass-client",
    };

    let formBody = [];
    for (let property in details) {
      const encodedKey = encodeURIComponent(property);
      const encodedValue = encodeURIComponent(details[property]);
      formBody.push(encodedKey + "=" + encodedValue);
    }
    formBody = formBody.join("&");
    this.setState({ isLoading: true })

    fetch(`${process.env.REACT_APP_AUTHORIZATION_BASE_URL}/oauth2/token`, {
      method: "POST",
      body: formBody,
      headers: {
        "content-type": "application/x-www-form-urlencoded;charset=UTF-8",
        Authorization: "Basic " + authorizationToken,
      },
    })
      .then((res) => res.json())
      .then((data) => {
        this.handleSuccessResponse(data)
      })
      .catch((error) => {
        NotificationManager.error(error, '', 5000)
        this.setState({ isLoading: false })
      });
  };

  componentDidMount() {
    setTimeout(() => {
      this.setState({ userNameControl: 'op1' })
    }, 2300)
  }

  render() {
    const {
      isLoading,
      userNameControl
    } = this.state
    return (
      <div className="loginForm">
        {isLoading && (
          <div className="loaderOverlay">
            <ReactSVG className="loader" src={`${SVG_LOADER}`} title="" alt="" />
          </div>
        )}
        <div className="central_form">
          <div id="userNameControl" className={`form-element op0 ${userNameControl}`}>
            <div className='sub_console'>
              <div className={`pressEnter justify-content-end`}>
                <OAuth2Login
                  className='oAuth lnk'
                  authorizationUrl={`${process.env.REACT_APP_AUTHORIZATION_BASE_URL}/oauth2/authorize`}
                  responseType="code"
                  clientId={process.env.REACT_APP_CLIENT_ID}
                  redirectUri={`${window.location.origin}/callback`}
                  scope="openid"
                  isCrossOrigin={false}
                  onSuccess={this.onSuccess}
                  onFailure={() => {
                    NotificationManager.error('APIs end point not working', '', 5000)
                  }}
                >
                  <span>Press To Login</span>&nbsp;&nbsp;
                  <EnterSVG />
                </OAuth2Login>
              </div>
            </div>
          </div>
        </div>
      </div>
    )
  }
}

LoginForm.propTypes = {
  apiEndPointUrl: PropTypes.string
}

export default withRouter(LoginForm);
