import userRoles from 'reusable-components/dist/utils/userRoles'
import {
  IC_OPERATIONAL,
  IC_STRATEGIC,
  IC_CLIENT,
} from 'reusable-components/dist/const/imgConst'

export const LandingPageLinks = () => {
  return [
    {
      title: 'PM/TechLeads',
      access: [userRoles.ADMIN, userRoles.MANAGER],
      link: {
        icon: IC_OPERATIONAL,
        linkTitle: 'Operational',
        linkUrl: '/pmo/overview/',
      },
    },
    {
      title: 'Client',
      access: [userRoles.ADMIN, userRoles.CLIENT],
      link: {
        icon: IC_CLIENT,
        linkTitle: 'Client',
        linkUrl: '/client/overview',
      },
    },
  ]
}
