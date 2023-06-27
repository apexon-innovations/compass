
jest.mock('react-dom/client', () => ({ createRoot: () => ({ render: jest.fn() }) }))

describe('Application root', () => {
  it('should render without crashing', () => {
    const div = document.createElement('div')
    div.id = 'root'
    document.body.appendChild(div)
    require('../index.js')
  })
})
