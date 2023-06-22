import { NotificationManager } from 'react-notifications'
import anime from 'animejs/lib/anime.es.js'
import userRoles from 'reusable-components/dist/utils/userRoles'
import { setIntoLocalStorage } from 'reusable-components/dist/utils/loginFunction'

export function getAnimation(marginTop = 0, marginBottom = 0) {
  anime({
    targets: '.userTextBox',
    marginTop: marginTop,
    marginBottom: marginBottom,
    align: 'center',
    width: '100%',
    display: 'block',
    fontSize: 36,
    scale: '1',
    lineHeight: '100%',
    duration: 2000,
  })
}

export function handleLoginSuccess(props, response) {
  let { roles } = setIntoLocalStorage(response)
  NotificationManager.success('Logged in successfully!', '', 5000)
  if (roles && roles.length === 1) {
    const role = roles[0]
    redirectionRoutes(props, role)
  } else {
    props.navigate('/home')
  }
}

export const redirectionRoutes = (props, role) => {
  switch (role) {
    case userRoles.MANAGER:
      window.location = `/pmo/overview`
      break
    case userRoles.CLIENT:
      window.location = `/client/overview`
      break
    default:
      props.navigate('/home')
      break
  }
}
