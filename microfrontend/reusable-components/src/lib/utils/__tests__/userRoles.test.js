import userRoles from '../userRoles'

it('User Role', () => {
  expect(userRoles).toEqual({
    PM: 'PROJECT_MANAGER',
    PDO: 'PDO',
    CLIENT: 'PROJECT_CLIENT',
  })
})
