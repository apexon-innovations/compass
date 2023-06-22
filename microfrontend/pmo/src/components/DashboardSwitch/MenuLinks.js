
import { IC_STRATEGIC, IC_OVERVIEW, IC_OPERATIONAL } from '../../const/imgConst.js'

export const getMenuLink = (projectId) => {

  return [
    {
      elementPosition: 0,
      imageSrc: IC_OPERATIONAL,
      name: 'Operational',
      link: `/pmo/operational/${projectId}`
    },
    {
      elementPosition: 1,
      imageSrc: IC_OVERVIEW,
      name: 'Overview',
      link: `/pmo/overview/${projectId}`
    },
    {
      elementPosition: 2,
      imageSrc: IC_STRATEGIC,
      name: 'Strategic',
      link: `/pmo/strategic/${projectId}`
    },
    {
      elementPosition: 3,
      imageSrc: IC_OPERATIONAL,
      name: 'Operational',
      link: `/pmo/operational/${projectId}`
    },
    {
      elementPosition: 4,
      imageSrc: IC_OVERVIEW,
      name: 'Overview',
      link: `/pmo/overview/${projectId}`
    },
    {
      elementPosition: 5,
      imageSrc: IC_STRATEGIC,
      name: 'Strategic',
      link: `/pmo/strategic/${projectId}`
    }
  ]
}

