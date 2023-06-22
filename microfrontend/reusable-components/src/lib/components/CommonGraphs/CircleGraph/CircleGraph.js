import React from 'react'
import { Circle } from '@tiaanduplessis/react-progressbar'
import PropTypes from 'prop-types'

const CircleGraph = React.memo(({ config }) => {
  return (
    <Circle
      svgStyle={{
        transform: `rotate(${config.rotate || 0})`,
      }}
      text={{
        value: config.value,
        style: {
          fontSize: config.styleFontSize || '18px',
          fontWeight: config.styleFontWeight || '700',
          color: config.styleColor || '#24ff00',
          position: config.stylePosition || 'absolute',
          left: config.styleLeft || '50%',
          top: config.styleTop || '50%',
          textAlign: config.styleTextAlign || 'center',
          padding: config.stylePadding || 0,
          margin: config.styleMargin || 0,
          transform: {
            prefix: config.styleTransformPrefix || true,
            value: `translate(${config.styleTransformValue || '-50%, -50%'})`,
          },
        },
      }}
      progress={config.progress / 100}
      color={config.color || '#24ff00'}
      trailColor={config.trailColor || 'transparent'}
      strokeWidth={config.strokeWidth || 3}
      duration={config.duration || 2000}
      easing={config.easing || 'easeInOut'}
    />
  )
})

CircleGraph.propTypes = {
  config: PropTypes.object,
}

export default CircleGraph
