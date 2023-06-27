import dataMapper, { split, parse, getKeyValue, setKeyValue } from '../dataMapper'

it('SPLIT with complicated key', () => {
  const k = 'abc.def.ghi.j..k\\.l\\\\.m.'
  const expectedResult = ['abc', 'def', 'ghi', 'j', '', 'k.l\\\\', 'm', '']
  const result = split(k, '.')
  expect(expectedResult).toEqual(result)
})

it('PARSE with complicated key', () => {
  const k = 'abc[].def[42]+.ghi?.j..k\\.l\\\\.m.'
  const expectedResult = [
    { name: 'abc' },
    { ix: '' },
    { name: 'def' },
    { ix: '42', add: true },
    { name: 'ghi', nulls: true },
    { name: 'j' },
    { name: 'k.l\\\\' },
    { name: 'm' },
  ]
  const result = parse(k, '.')
  expect(expectedResult).toEqual(result)
})

it('PARSE with simple key', () => {
  const k = 'abc'
  const expectedResult = [{ name: 'abc' }]
  const result = parse(k)
  expect(expectedResult).toEqual(result)
})

it('PARSE abc? (simple key allowing nulls)', () => {
  const k = 'abc?'
  const expectedResult = [{ name: 'abc', nulls: true }]
  const result = parse(k)
  expect(expectedResult).toEqual(result)
})

it('PARSE with simple empty array key', () => {
  const k = 'abc[]'
  const expectedResult = [{ name: 'abc' }, { ix: '' }]
  const result = parse(k)
  expect(expectedResult).toEqual(result)
})

it('PARSE with no key empty array key', () => {
  const k = '[]'
  const expectedResult = [{ ix: '' }]
  const result = parse(k)
  expect(expectedResult).toEqual(result)
})

it('PARSE with nothing', () => {
  const k = ''
  const expectedResult = []
  const result = parse(k)
  expect(expectedResult).toEqual(result)
})

it('PARSE with simple dot notation key', () => {
  const k = 'abc.def'
  const expectedResult = [{ name: 'abc' }, { name: 'def' }]
  const result = parse(k)
  expect(expectedResult).toEqual(result)
})

it('PARSE with deep dot notation key', () => {
  const k = 'a.b.c.d.e.f'
  const expectedResult = [
    { name: 'a' },
    { name: 'b' },
    { name: 'c' },
    { name: 'd' },
    { name: 'e' },
    { name: 'f' },
  ]
  const result = parse(k)
  expect(expectedResult).toEqual(result)
})

it('PARSE with deep brackets', () => {
  const k = 'abc[].def'
  const expectedResult = [{ name: 'abc' }, { ix: '' }, { name: 'def' }]
  const result = parse(k)
  expect(expectedResult).toEqual(result)
})

it('PARSE with deep brackets and instruction to add together', () => {
  const k = 'abc[]+.def'
  const expectedResult = [{ name: 'abc' }, { ix: '', add: true }, { name: 'def' }]
  const result = parse(k)
  expect(expectedResult).toEqual(result)
})

it('PARSE with deep brackets and instruction to add nulls', () => {
  const k = 'abc[]+.def?'
  const expectedResult = [{ name: 'abc' }, { ix: '', add: true }, { name: 'def', nulls: true }]
  const result = parse(k)
  expect(expectedResult).toEqual(result)
})

it('PARSE with deep brackets', () => {
  const k = '[].def'
  const expectedResult = [{ ix: '' }, { name: 'def' }]
  const result = parse(k)
  expect(expectedResult).toEqual(result)
})
it('MAP with empty default on missing key', () => {
  const obj = { foo: 'bar' }
  const map = { undefined_key: { key: 'key_with_default', default: '' } }
  const expectedResult = { key_with_default: '' }

  const result = dataMapper(obj, map)
  expect(expectedResult).toEqual(result)
})

it('MAP with null default on missing key', () => {
  const obj = { foo: 'bar' }
  const map = { undefined_key: { key: 'key_with_default', default: null } }
  const expectedResult = { key_with_default: null }

  const result = dataMapper(obj, map)
  expect(expectedResult).toEqual(result)
})

it('MAP - multiple levels of array indexes on both the from and to arrays', () => {
  const obj = {
    Items: [
      {
        SubItems: [{ SubKey: 'item 1 id a' }, { SubKey: 'item 1 id b' }],
      },
      {
        SubItems: [{ SubKey: 'item 2 id a' }, { SubKey: 'item 2 id b' }],
      },
    ],
  }
  const expectedResult = {
    items: [
      {
        subitems: [{ subkey: 'item 1 id a' }, { subkey: 'item 1 id b' }],
      },
      {
        subitems: [{ subkey: 'item 2 id a' }, { subkey: 'item 2 id b' }],
      },
    ],
  }
  const map = {
    'Items[].SubItems[].SubKey': 'items[].subitems[].subkey',
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('get value - one level deep', () => {
  const obj = { foo: { bar: 'baz' } }
  const map = 'foo.bar'
  const expectedResult = 'baz'
  const result = getKeyValue(obj, map)
  expect(expectedResult).toEqual(result)
})

it('get value - starting with simple array', () => {
  const obj = ['bar']
  const map = '[]'
  const expectedResult = ['bar']
  const result = getKeyValue(obj, map)

  expect(expectedResult).toEqual(result)
})

it('get value - simple array defined index', () => {
  const obj = ['foo', 'bar']
  const map = '[1]'
  const expectedResult = 'bar'
  const result = getKeyValue(obj, map)

  expect(expectedResult).toEqual(result)
})

it('get value - simple array negative index', () => {
  const obj = ['foo', 'bar']
  const map = '[-1]'
  const expectedResult = 'bar'
  const result = getKeyValue(obj, map)

  expect(expectedResult).toEqual(result)
})

it('get value - simple array dot property', () => {
  const obj = [{ name: 'foo' }, { name: 'bar' }]
  const map = '[-1].name'
  const expectedResult = 'bar'
  const result = getKeyValue(obj, map)

  expect(expectedResult).toEqual(result)
})

it('get value - simple array negative index falls off array', () => {
  const obj = ['foo', 'bar']
  const map = '[-3]'
  const expectedResult = null
  const result = getKeyValue(obj, map)

  expect(expectedResult).toEqual(result)
})

it('get value - two levels deep', () => {
  const key = 'foo.baz.fog'

  const obj = {
    foo: {
      baz: {
        fog: 'bar',
      },
    },
  }

  const expectedResult = 'bar'

  const result = getKeyValue(obj, key)

  expect(expectedResult).toEqual(result)
})

it('get value - one level deep and item is a array', () => {
  const key = 'foo.baz[]'

  const obj = {
    foo: {
      baz: ['bar'],
    },
  }

  const expectedResult = ['bar']

  const result = getKeyValue(obj, key)

  expect(expectedResult).toEqual(result)
})

it('get value - one level deep and first item of array', () => {
  const key = 'foo.baz[1]'

  const obj = {
    foo: {
      baz: ['bar', 'foo'],
    },
  }

  const expectedResult = 'foo'

  const result = getKeyValue(obj, key)

  expect(expectedResult).toEqual(result)
})

it('get value - one level deep and array and one level', () => {
  const key = 'foo.baz[].fog'

  const obj = {
    foo: {
      baz: [
        {
          fog: 'bar',
        },
      ],
    },
  }

  const expectedResult = ['bar']

  const result = getKeyValue(obj, key)

  expect(expectedResult).toEqual(result)
})

it('get value - one level deep and first item of array and one level', () => {
  const key = 'foo.baz[0].fog'

  const obj = {
    foo: {
      baz: [
        {
          fog: 'bar',
        },
      ],
    },
  }

  const expectedResult = 'bar'

  const result = getKeyValue(obj, key)

  expect(expectedResult).toEqual(result)
})

it('get value - one level deep and first item of array and two levels', () => {
  const key = 'foo.baz[0].fog.baz'

  const obj = {
    foo: {
      baz: [
        {
          fog: {
            baz: 'bar',
          },
        },
      ],
    },
  }

  const expectedResult = 'bar'

  const result = getKeyValue(obj, key)

  expect(expectedResult).toEqual(result)
})

it('get value - one level array', () => {
  const key = 'foo[]'

  const obj = {
    foo: [
      {
        baz: [
          {
            fog: {
              baz: 'bar',
            },
          },
        ],
      },
    ],
  }

  const expectedResult = [
    {
      baz: [
        {
          fog: {
            baz: 'bar',
          },
        },
      ],
    },
  ]

  const result = getKeyValue(obj, key)

  expect(expectedResult).toEqual(result)
})

it('get value - two level deep array', () => {
  const key = 'foo[].baz[].fog.baz'

  const obj = {
    foo: [
      {
        baz: [{ fog: { baz: 'bar' } }, { fog: { baz: 'const' } }],
      },
    ],
  }

  const expectedResult = [['bar', 'const']]

  const result = getKeyValue(obj, key)

  expect(expectedResult).toEqual(result)
})

it('get value - crazy', () => {
  const key = 'foo.baz[0].fog[1].baz'

  const obj = {
    foo: {
      baz: [
        {
          fog: [
            ,
            {
              baz: 'bar',
            },
          ],
        },
      ],
    },
  }

  const expectedResult = 'bar'

  const result = getKeyValue(obj, key)

  expect(expectedResult).toEqual(result)
})

it('get value - crazy negative', () => {
  const key = 'foo.baz[-1].fog[1].baz'

  const obj = {
    foo: {
      baz: [
        {
          fog: [
            ,
            {
              baz: 'bar',
            },
          ],
        },
      ],
    },
  }

  const expectedResult = 'bar'

  const result = getKeyValue(obj, key)

  expect(expectedResult).toEqual(result)
})

it('select with array object where map is not an array 1', () => {
  const obj = { foo: [{ bar: 'a' }, { bar: 'b' }, { bar: 'c' }] }
  const map = 'foo.bar'
  const expectedResult = 'a'
  const result = getKeyValue(obj, map)
  expect(expectedResult).toEqual(result)
})

it('select with array object where map is not an array 2', () => {
  const obj = { foo: [{ bar: 'a' }, { bar: 'b' }, { bar: 'c' }] }
  const map = 'foo[].bar'
  const expectedResult = ['a', 'b', 'c']
  const result = getKeyValue(obj, map)
  expect(expectedResult).toEqual(result)
})

it('set value - simple', () => {
  const key = 'foo'
  const value = 'bar'
  const expectedResult = {
    foo: 'bar',
  }

  const result = setKeyValue(null, key, value)

  expect(expectedResult).toEqual(result)
})

it('set value - simple with base object', () => {
  const key = 'foo'
  const value = 'bar'

  const base = {
    baz: 'foo',
  }

  const expectedResult = {
    baz: 'foo',
    foo: 'bar',
  }

  const result = setKeyValue(base, key, value)

  expect(expectedResult).toEqual(result)
})

it('set value - simple array', () => {
  const key = '[]'
  const value = 'bar'

  const expectedResult = ['bar']

  const result = setKeyValue(null, key, value)

  expect(expectedResult).toEqual(result)
})

it('set value - simple array with base array', () => {
  const key = '[]'
  const value = 'bar'

  const base = ['foo']
  const expectedResult = ['bar']

  const result = setKeyValue(base, key, value)

  expect(expectedResult).toEqual(result)
})

it('set value - simple array in index 0', () => {
  const map = '[0]'
  const data = 'bar'

  const expectedResult = ['bar']

  const result = setKeyValue(null, map, data)

  expect(expectedResult).toEqual(result)
})

it('set value - simple array in index 0 with base array', () => {
  const key = '[0]'
  const value = 'bar'

  const base = ['foo']
  const expectedResult = ['bar']

  const result = setKeyValue(base, key, value)

  expect(expectedResult).toEqual(result)
})

it('set value - simple array in index 1', () => {
  const map = '[1]'
  const data = 'bar'

  const expectedResult = [, 'bar']

  const result = setKeyValue(null, map, data)

  expect(expectedResult).toEqual(result)
})

it('set value - one level deep', () => {
  const key = 'foo.bar'
  const value = 'baz'

  const expectedResult = {
    foo: {
      bar: 'baz',
    },
  }

  const result = setKeyValue({}, key, value)

  expect(expectedResult).toEqual(result)
})

it('set value - object inside simple array', () => {
  const key = '[].foo'
  const value = 'bar'

  const expectedResult = [
    {
      foo: 'bar',
    },
  ]

  const result = setKeyValue(null, key, value)

  expect(expectedResult).toEqual(result)
})

it('set value - array to object inside simple array', () => {
  const key = '[].foo'
  const value = ['bar', 'baz']

  const expectedResult = [
    {
      foo: 'bar',
    },
    {
      foo: 'baz',
    },
  ]

  const result = setKeyValue(null, key, value)

  expect(expectedResult).toEqual(result)
})

it('set value - object inside simple array defined index', () => {
  const key = '[3].foo'
  const data = 'bar'

  const expectedResult = [
    ,
    ,
    ,
    {
      foo: 'bar',
    },
  ]

  const result = setKeyValue(null, key, data)

  expect(expectedResult).toEqual(result)
})

it('set value - two levels deep', () => {
  const key = 'foo.bar.baz'
  const value = 'foo'

  const expectedResult = {
    foo: {
      bar: {
        baz: 'foo',
      },
    },
  }

  const result = setKeyValue({}, key, value)

  expect(expectedResult).toEqual(result)
})

it('set value - one level deep inside array', () => {
  const key = 'foo.bar[]'
  const value = 'baz'

  const expectedResult = {
    foo: {
      bar: ['baz'],
    },
  }

  const result = setKeyValue({}, key, value)

  expect(expectedResult).toEqual(result)
})

it('set value - one level deep inside array with one level deep', () => {
  const key = 'foo.bar[].baz'
  const value = 'foo'

  const expectedResult = {
    foo: {
      bar: [
        {
          baz: 'foo',
        },
      ],
    },
  }

  const result = setKeyValue({}, key, value)

  expect(expectedResult).toEqual(result)
})

it('set value - one level deep inside array with one level deep inside a existing array', () => {
  const key = 'foo.bar[].baz'
  const value = 'foo'

  const base = {
    foo: {
      bar: [
        {
          bar: 'baz',
        },
      ],
    },
  }

  const expectedResult = {
    foo: {
      bar: [
        {
          bar: 'baz',
          baz: 'foo',
        },
      ],
    },
  }

  const result = setKeyValue(base, key, value)

  expect(expectedResult).toEqual(result)
})

it('set value - one level deep inside array at defined index with one level deep', () => {
  const key = 'foo.bar[1].baz'
  const value = 'foo'

  const expectedResult = {
    foo: {
      bar: [
        ,
        {
          baz: 'foo',
        },
      ],
    },
  }

  const result = setKeyValue({}, key, value)

  expect(expectedResult).toEqual(result)
})

it('set value - array to simple object', () => {
  const key = 'foo[].baz'
  const value = ['foo', 'const']

  const expectedResult = {
    foo: [
      {
        baz: 'foo',
      },
      {
        baz: 'const',
      },
    ],
  }

  const result = setKeyValue({}, key, value)

  expect(expectedResult).toEqual(result)
})

it('set value - array to two level object', () => {
  const key = 'bar.foo[].baz'
  const value = ['foo', 'const']

  const expectedResult = {
    bar: {
      foo: [
        {
          baz: 'foo',
        },
        {
          baz: 'const',
        },
      ],
    },
  }

  const result = setKeyValue({}, key, value)

  expect(expectedResult).toEqual(result)
})

it('set value - array to two level object', () => {
  const key = 'bar.foo[].baz.foo'
  const value = ['foo', 'const']

  const expectedResult = {
    bar: {
      foo: [
        {
          baz: {
            foo: 'foo',
          },
        },
        {
          baz: {
            foo: 'const',
          },
        },
      ],
    },
  }

  const result = setKeyValue({}, key, value)

  expect(expectedResult).toEqual(result)
})

it('set value - array to object', () => {
  const key = 'foo[].bar[].baz'
  const value = [['foo', 'const']]

  const expectedResult = {
    foo: [
      {
        bar: [
          {
            baz: 'foo',
          },
          {
            baz: 'const',
          },
        ],
      },
    ],
  }

  const result = setKeyValue({}, key, value)

  expect(expectedResult).toEqual(result)
})

it('set value - crazy', () => {
  const key = 'foo.bar[1].baz[2].thing'
  const value = 'foo'

  const expectedResult = {
    foo: {
      bar: [
        ,
        {
          baz: [
            ,
            ,
            {
              thing: 'foo',
            },
          ],
        },
      ],
    },
  }

  const result = setKeyValue({}, key, value)

  expect(expectedResult).toEqual(result)
})

it('map object to another - simple', () => {
  const obj = {
    foo: 'bar',
  }

  const expectedResult = {
    bar: 'bar',
  }

  const map = {
    foo: 'bar',
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('map object to another - complexity 1', () => {
  const obj = {
    foo: {
      bar: 'baz',
    },
  }

  const expectedResult = {
    bar: {
      foo: 'baz',
    },
  }

  const map = {
    'foo.bar': 'bar.foo',
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('map object to another - complexity 2', () => {
  const obj = {
    foo: {
      bar: 'baz',
    },
  }

  const expectedResult = {
    bar: {
      foo: [
        {
          baz: 'baz',
        },
      ],
    },
  }

  const map = {
    'foo.bar': 'bar.foo[].baz',
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('map object to another - with base object', () => {
  const baseObject = {
    test: 1,
  }

  const obj = {
    foo: {
      bar: 'baz',
    },
  }

  const expectedResult = {
    test: 1,
    bar: {
      foo: [
        {
          baz: 'baz',
        },
      ],
    },
  }

  const map = {
    'foo.bar': 'bar.foo[].baz',
  }

  const result = dataMapper(obj, baseObject, map)

  expect(expectedResult).toEqual(result)
})

it('map object to another - with two destinations for same value', () => {
  const to_obj = {
    test: 1,
  }

  const from_obj = {
    foo: 'bar',
  }

  const expectedResult = {
    test: 1,
    bar: 'bar',
    baz: 'bar',
  }

  const map = {
    foo: ['bar', 'baz'],
  }

  const result = dataMapper(from_obj, to_obj, map)

  expect(expectedResult).toEqual(result)
})
it('map object to another - with two destinations for same value inside object', () => {
  const baseObject = {
    test: 1,
  }

  const obj = {
    foo: {
      bar: 'baz',
    },
  }

  const expectedResult = {
    test: 1,
    bar: {
      foo: {
        baz: 'baz',
        foo: 'baz',
      },
    },
  }

  const map = {
    'foo.bar': ['bar.foo.baz', 'bar.foo.foo'],
  }

  const result = dataMapper(obj, baseObject, map)

  expect(expectedResult).toEqual(result)
})
it('map object to another - with two destinations for same value inside array', () => {
  const baseObject = {
    test: 1,
  }

  const obj = {
    foo: {
      bar: 'baz',
    },
  }

  const expectedResult = {
    test: 1,
    bar: {
      foo: [
        {
          baz: 'baz',
          foo: 'baz',
        },
      ],
    },
  }

  const map = {
    'foo.bar': ['bar.foo[].baz', 'bar.foo[].foo'],
  }

  const result = dataMapper(obj, baseObject, map)

  expect(expectedResult).toEqual(result)
})

it('map object to another - with three destinations for same value', () => {
  const baseObject = {
    test: 1,
  }

  const obj = {
    foo: {
      bar: 'baz',
    },
  }

  const expectedResult = {
    test: 1,
    bar: {
      foo: [
        {
          baz: 'baz',
          foo: 'baz',
          bar: ['baz'],
        },
      ],
    },
  }

  const map = {
    'foo.bar': ['bar.foo[].baz', 'bar.foo[].foo', 'bar.foo[].bar[]'],
  }

  const result = dataMapper(obj, baseObject, map)

  expect(expectedResult).toEqual(result)
})

it('map object to another - with key object notation', () => {
  const to_obj = {
    test: 1,
  }

  const from_obj = {
    foo: {
      bar: 'baz',
    },
  }

  const expectedResult = {
    test: 1,
    bar: {
      foo: [
        {
          baz: 'baz',
        },
      ],
    },
  }

  const map = {
    'foo.bar': {
      key: 'bar.foo[].baz',
    },
  }

  const result = dataMapper(from_obj, to_obj, map)

  expect(expectedResult).toEqual(result)
})

it('map object to another - with key object notation with default value when key does not exists', () => {
  const baseObject = {
    test: 1,
  }

  const obj = {
    foo: {
      bar: 'baz',
    },
  }

  const expectedResult = {
    test: 1,
    bar: {
      foo: [
        {
          baz: 10,
        },
      ],
    },
  }

  const map = {
    notExistingKey: {
      key: 'bar.foo[].baz',
      default: 10,
    },
  }

  const result = dataMapper(obj, baseObject, map)

  expect(expectedResult).toEqual(result)
})

it('map object to another - with key object notation with default function when key does not exists', () => {
  const baseObject = {
    test: 1,
  }

  const obj = {
    foo: {
      bar: 'baz',
    },
  }

  const expectedResult = {
    test: 1,
    bar: {
      foo: [
        {
          baz: 'baz',
        },
      ],
    },
  }

  const map = {
    notExistingKey: {
      key: 'bar.foo[].baz',
      default: function(fromObject, fromKey, toObject, toKey) {
        return fromObject.foo.bar
      },
    },
  }

  const result = dataMapper(obj, baseObject, map)

  expect(expectedResult).toEqual(result)
})

it('map object to another - when target key is undefined it should be ignored', () => {
  const obj = {
    a: 1234,
    foo: {
      bar: 'baz',
    },
  }

  const expectedResult = {
    bar: {
      bar: 'baz',
    },
  }

  const map = {
    'foo.bar': 'bar.bar',
    a: undefined,
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('map object to another - with key object notation with default function returning undefined when key does not exists', () => {
  const obj = {
    a: 1234,
    foo: {
      bar: 'baz',
    },
  }

  const expectedResult = {
    bar: {
      bar: 'baz',
      a: 1234,
    },
  }

  const map = {
    'foo.bar': 'bar.bar',
    notExistingKey: {
      key: 'bar.test',
      default: function(fromObject, fromKey, toObject, toKey) {
        return undefined
      },
    },
    a: 'bar.a',
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('map object to another - with key object notation with transform', () => {
  const baseObject = {
    test: 1,
  }

  const obj = {
    foo: {
      bar: 'baz',
    },
  }

  const expectedResult = {
    test: 1,
    bar: {
      foo: [
        {
          baz: 'baz-foo',
        },
      ],
    },
  }

  const map = {
    'foo.bar': {
      key: 'bar.foo[].baz',
      transform: function(value, fromObject, toObject, fromKey, toKey) {
        return value + '-foo'
      },
    },
  }

  const result = dataMapper(obj, baseObject, map)

  expect(expectedResult).toEqual(result)
})

it('map object to another - with two destinations for same value one string and one object', () => {
  const baseObject = {
    test: 1,
  }

  const obj = {
    foo: {
      bar: 'baz',
    },
  }

  const expectedResult = {
    test: 1,
    bar: {
      foo: [
        {
          baz: 'baz',
          foo: 'baz-foo',
        },
      ],
    },
  }

  const map = {
    'foo.bar': [
      'bar.foo[].baz',
      {
        key: 'bar.foo[].foo',
        transform: function(value, fromObject, toObject, fromKey, toKey) {
          return value + '-foo'
        },
      },
    ],
  }

  const result = dataMapper(obj, baseObject, map)

  expect(expectedResult).toEqual(result)
})

it('map object to another - with key array notation', () => {
  const baseObject = {
    test: 1,
  }

  const obj = {
    foo: {
      bar: 'baz',
    },
  }

  const expectedResult = {
    test: 1,
    bar: {
      foo: [
        {
          baz: 'baz',
        },
      ],
    },
  }

  const map = {
    'foo.bar': [['bar.foo[].baz']],
  }

  const result = dataMapper(obj, baseObject, map)

  expect(expectedResult).toEqual(result)
})
it('map object to another - with key array notation with default value when key does not exists', () => {
  const baseObject = {
    test: 1,
  }

  const obj = {
    foo: {
      bar: 'baz',
    },
  }

  const expectedResult = {
    test: 1,
    bar: {
      foo: [
        {
          baz: 10,
        },
      ],
    },
  }

  const map = {
    notExistingKey: [['bar.foo[].baz', null, 10]],
  }

  const result = dataMapper(obj, baseObject, map)

  expect(expectedResult).toEqual(result)
})

it('map object to another - with key array notation with default function when key does not exists', () => {
  const baseObject = {
    test: 1,
  }

  const obj = {
    foo: {
      bar: 'baz',
    },
  }

  const expectedResult = {
    test: 1,
    bar: {
      foo: [
        {
          baz: 'baz',
        },
      ],
    },
  }

  const map = {
    notExistingKey: [
      [
        'bar.foo[].baz',
        null,
        function(fromObject, fromKey, toObject, toKey) {
          return fromObject.foo.bar
        },
      ],
    ],
  }

  const result = dataMapper(obj, baseObject, map)

  expect(expectedResult).toEqual(result)
})

it('map object to another - with key array notation with transform function', () => {
  const baseObject = {
    test: 1,
  }

  const obj = {
    foo: {
      bar: 'baz',
    },
  }

  const expectedResult = {
    test: 1,
    bar: {
      foo: [
        {
          baz: 'baz-foo',
        },
      ],
    },
  }

  const map = {
    'foo.bar': [
      [
        'bar.foo[].baz',
        function(value, fromObject, toObject, fromKey, toKey) {
          return value + '-foo'
        },
      ],
    ],
  }

  const result = dataMapper(obj, baseObject, map)

  expect(expectedResult).toEqual(result)
})

it('map object to another - map object without destination key via transform', () => {
  const obj = {
    thing: {
      thing2: {
        thing3: {
          a: 'a1',
          b: 'b1',
        },
      },
    },
  }

  const map = {
    'thing.thing2.thing3': [
      [
        null,
        function(val, src, dst) {
          dst.manual = val.a + val.b
        },
        null,
      ],
    ],
  }

  const expectedResult = {
    manual: 'a1b1',
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('array mapping - simple', () => {
  const obj = {
    comments: [
      { a: 'a1', b: 'b1' },
      { a: 'a2', b: 'b2' },
    ],
  }

  const map = {
    'comments[].a': ['comments[].c'],
    'comments[].b': ['comments[].d'],
  }

  const expectedResult = {
    comments: [
      { c: 'a1', d: 'b1' },
      { c: 'a2', d: 'b2' },
    ],
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('array mapping - two level deep', () => {
  const obj = {
    comments: [
      {
        data: [
          { a: 'a1', b: 'b1' },
          { a: 'a2', b: 'b2' },
        ],
      },
    ],
  }

  const map = {
    'comments[].data[].a': 'comments[].data[].c',
    'comments[].data[].b': 'comments[].data[].d',
  }

  const expectedResult = {
    comments: [
      {
        data: [
          { c: 'a1', d: 'b1' },
          { c: 'a2', d: 'b2' },
        ],
      },
    ],
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('array mapping - simple deep', () => {
  const obj = {
    thing: {
      comments: [
        { a: 'a1', b: 'b1' },
        { a: 'a2', b: 'b2' },
      ],
    },
  }

  const map = {
    'thing.comments[].a': ['thing.comments[].c'],
    'thing.comments[].b': ['thing.comments[].d'],
  }

  const expectedResult = {
    thing: {
      comments: [
        { c: 'a1', d: 'b1' },
        { c: 'a2', d: 'b2' },
      ],
    },
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('array mapping - from/to specific indexes', () => {
  const obj = {
    comments: [
      { a: 'a1', b: 'b1' },
      { a: 'a2', b: 'b2' },
    ],
  }

  const map = {
    'comments[0].a': ['comments[1].c'],
    'comments[0].b': ['comments[1].d'],
  }

  const expectedResult = {
    comments: [, { c: 'a1', d: 'b1' }],
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('array mapping - fromObject is an array', () => {
  const obj = [
    { a: 'a1', b: 'b1' },
    { a: 'a2', b: 'b2' },
  ]

  const map = {
    '[].a': '[].c',
    '[].b': '[].d',
  }

  const expectedResult = [
    { c: 'a1', d: 'b1' },
    { c: 'a2', d: 'b2' },
  ]

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('mapping - map full array to single value via transform', () => {
  const obj = {
    thing: [
      { a: 'a1', b: 'b1' },
      { a: 'a2', b: 'b2' },
      { a: 'a3', b: 'b3' },
    ],
  }

  const map = {
    thing: [
      [
        'thing2',
        function(val, src, dst) {
          const a = val.reduce(function(i, obj) {
            return (i += obj.a)
          }, '')

          return a
        },
        null,
      ],
    ],
  }

  const expectedResult = {
    thing2: 'a1a2a3',
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('mapping - map full array without destination key via transform', () => {
  const obj = {
    thing: {
      thing2: {
        thing3: [
          { a: 'a1', b: 'b1' },
          { a: 'a2', b: 'b2' },
          { a: 'a3', b: 'b3' },
        ],
      },
    },
  }

  const map = {
    'thing.thing2.thing3': [
      [
        null,
        function(val, src, dst) {
          const a = val.reduce(function(i, obj) {
            return (i += obj.a)
          }, '')

          dst.manual = a
        },
        null,
      ],
    ],
  }

  const expectedResult = {
    manual: 'a1a2a3',
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('mapping - map full array to same array on destination side', () => {
  const obj = {
    thing: [
      { a: 'a1', b: 'b1' },
      { a: 'a2', b: 'b2' },
      { a: 'a3', b: 'b3' },
    ],
  }

  const map = {
    thing: 'thing2',
  }

  const expectedResult = {
    thing2: [
      { a: 'a1', b: 'b1' },
      { a: 'a2', b: 'b2' },
      { a: 'a3', b: 'b3' },
    ],
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('mapping - map and append full array to existing mapped array', () => {
  const obj = {
    thing: [
      { a: 'a1', b: 'b1' },
      { a: 'a2', b: 'b2' },
      { a: 'a3', b: 'b3' },
    ],
    thingOther: [
      { a: 'a4', b: 'b4' },
      { a: 'a5', b: 'b5' },
      { a: 'a6', b: 'b6' },
    ],
  }

  const map = {
    thing: 'thing2[]+',
    thingOther: 'thing2[]+',
  }

  const expectedResult = {
    thing2: [
      [
        { a: 'a1', b: 'b1' },
        { a: 'a2', b: 'b2' },
        { a: 'a3', b: 'b3' },
      ],
      [
        { a: 'a4', b: 'b4' },
        { a: 'a5', b: 'b5' },
        { a: 'a6', b: 'b6' },
      ],
    ],
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('map object to another - prevent null values from being mapped', () => {
  const obj = {
    a: 1234,
    foo: {
      bar: null,
    },
  }

  const expectedResult = {
    foo: {
      a: 1234,
    },
  }

  const map = {
    'foo.bar': 'bar.bar',
    a: 'foo.a',
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('map object to another - allow null values', () => {
  const obj = {
    a: 1234,
    foo: {
      bar: null,
    },
  }

  const expectedResult = {
    foo: {
      a: 1234,
    },
    bar: null,
  }

  const map = {
    'foo.bar': 'bar?',
    a: 'foo.a',
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('map object to another - allow null values', () => {
  const obj = {
    a: 1234,
    foo: {
      bar: null,
    },
  }

  const expectedResult = {
    foo: {
      a: 1234,
    },
    bar: {
      bar: null,
    },
  }

  const map = {
    'foo.bar': 'bar.bar?',
    a: 'foo.a',
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('Mapping destination property with a literal dot', () => {
  const obj = {
    foo: {
      bar: 'baz',
    },
  }

  const expectedResult = {
    'bar.baz': 'baz',
  }

  const map = {
    'foo.bar': {
      key: 'bar\\.baz',
      transform: function(value, fromObject, toObject, fromKey, toKey) {
        return value
      },
    },
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('Mapping destination property with wrong escaped dot', () => {
  const obj = {
    foo: {
      bar: 'baz',
    },
  }

  const expectedResult = {
    bar: { baz: 'baz' },
  }

  const map = {
    'foo.bar': {
      key: 'bar.baz', // actually equivalent to bar.baz as "bar\.baz" === "bar.baz"
      transform: function(value, fromObject, toObject, fromKey, toKey) {
        return value
      },
    },
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('Mapping destination property with two escaped dots', () => {
  const obj = {
    foo: {
      bar: 'baz',
    },
  }

  const expectedResult = {
    'bar.baz.duz': 'baz',
  }

  const map = {
    'foo.bar': {
      key: 'bar\\.baz\\.duz',
      transform: function(value, fromObject, toObject, fromKey, toKey) {
        return value
      },
    },
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('Mapping destination property with backslash itself escaped', () => {
  const obj = {
    foo: {
      bar: 'baz',
    },
  }

  const expectedResult = {
    'bar\\\\': { baz: 'baz' },
  }
  const map = {
    'foo.bar': {
      key: 'bar\\\\.baz',
      transform: function(value, fromObject, toObject, fromKey, toKey) {
        return value
      },
    },
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('Mapping properties with glob patterns', () => {
  const obj = {
    nodes: {
      db_node: {
        type: 'db',
        image: 'mongodb',
      },
      app_node: {
        type: 'app',
        image: 'nginx',
      },
    },
  }

  const expectedResult = {
    types: ['db', 'app'],
  }
  const map = {
    'nodes.*.type': 'types',
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('Mapping properties with glob patterns with incomplete path', () => {
  const obj = {
    nodes: {
      db_node: {
        type: 'db',
        image: 'mongodb',
      },
      app_node: {
        type: 'app',
        image: 'nginx',
      },
    },
  }

  const expectedResult = {
    types: [
      {
        type: 'db',
        image: 'mongodb',
      },
      {
        type: 'app',
        image: 'nginx',
      },
    ],
  }
  const map = {
    'nodes.*': 'types',
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('Multi-level array issue #29', () => {
  const orig = {
    foo: [
      { name: 'a', things: ['a1', 'a2'] },
      { name: 'b', things: ['b1', 'b2'] },
    ],
  }
  const map = {
    'foo[].name': 'bar[].label',
    'foo[].things[]': 'bar[].values[]',
  }

  const expectedResult = {
    bar: [
      {
        label: 'a',
        values: ['a1', 'a2'],
      },
      {
        label: 'b',
        values: ['b1', 'b2'],
      },
    ],
  }

  const result = dataMapper(orig, map)

  expect(expectedResult).toEqual(result)
})

it('Ensure that boolean values work for both arrays and objects #37', () => {
  const to_obj = {
    test: 1,
  }

  const from_obj = {
    foo: {
      bar: false,
      baz: [1, 2, 'three', false],
    },
  }

  const map = {
    'foo.bar': { key: 'baz' },
    'foo.baz': 'biff',
  }

  const expectedResult = {
    test: 1,
    baz: false,
    biff: [1, 2, 'three', false],
  }

  const result = dataMapper(from_obj, to_obj, map)

  expect(expectedResult).toEqual(result)
})

it('Ensure that multi-dimentional arrays work #41', () => {
  const src = {
    arr: [
      {
        id: 1,
      },
    ],
  }

  const map = {
    'arr[].id': 'arr[].id',
    'arr[].arr[].id': 'arr[].arr[].id',
    'arr[].arr[].arr[].id': 'arr[].arr[].arr[].id',
  }

  const expectedResult = { arr: [{ id: 1 }] }

  const result = dataMapper(src, map)

  expect(expectedResult).toEqual(result)
})

it('Make sure no objects are created without data #48', () => {
  const obj = {
    a: 1234,
    foo: {
      bar: null,
    },
  }

  const expectedResult = {
    foo: {
      a: 1234,
    },
  }

  const map = {
    'foo.bar': 'bar.bar',
    a: 'foo.a',
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('Should correctly map to a subelement of indexed array item.', () => {
  const obj = {
    nodes: [
      {
        type: 'db',
        image: 'mongodb',
      },
      {
        type: 'app',
        image: 'nginx',
      },
    ],
  }

  const expectedResult = {
    result: [
      {
        env: {
          nodes: [
            {
              type: 'db',
              image: 'mongodb',
            },
            {
              type: 'app',
              image: 'nginx',
            },
          ],
        },
      },
    ],
  }
  const map = {
    'nodes[].type': 'result[0].env.nodes[].type',
    'nodes[].image': 'result[0].env.nodes[].image',
  }

  const result = dataMapper(obj, map)

  expect(expectedResult).toEqual(result)
})

it('MAP Should correctly create an array and add if undlerlying data structure is object #64', () => {
  const src = { foo: { bar: 'baz' } }
  const map = { 'foo[].bar': 'abc[].def' }
  const expectedResult = { abc: [{ def: 'baz' }] }
  const result = dataMapper(src, map)

  expect(expectedResult).toEqual(result)
})

it('issue #71: mapping array should not fail when not defined', () => {
  const src = {}

  const map = {
    mySizes: [
      {
        key: 'sizes',
        transform: sizes => sizes.map(data => data),
        default: () => [],
      },
    ],
  }

  const expectedResult = { sizes: [] }

  const result = dataMapper(src, map)

  expect(expectedResult).toEqual(result)
})
