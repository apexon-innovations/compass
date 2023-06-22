import {
  IC_OVERVIEW,
  IC_OPERATIONAL,
} from 'reusable-components/dist/const/imgConst'

export const getMenuLink = () => {
  return [
    {
      activeClass: false,
      imageSrc: IC_OVERVIEW,
      name: 'Business',
      link: `/client/business-overview`,
    },
    {
      activeClass: true,
      imageSrc: IC_OPERATIONAL,
      name: 'Operational',
      link: `/client/overview`,
    },
  ]
}
