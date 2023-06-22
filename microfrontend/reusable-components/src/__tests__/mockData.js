export const mockJest = (status = 200, data = {}) => {
  global.fetch = () =>
    Promise.resolve({
      status,
      text: () => Promise.resolve(data),
    })
}
