import {
  animation,
  stagger,
  state,
  trigger,
  sequence,
  keyframes,
  animateChild,
  group,
  transition,
  animate,
  style,
  query
} from '@angular/animations';

export const transAnimation = animation([
  style({
    height: '{{ height }}',
    opacity: '{{ opacity }}',
    backgroundColor: '{{ backgroundColor }}'
  }),
  animate('{{ time }}')
]);

// Routable animations
export const slideInAnimation = trigger('routeAnimations', [
  transition('HomePage <=> AboutPage', [
    style({ position: 'relative' }),
    query(':enter, :leave', [
      style({
        position: 'absolute',
        top: 0,
        left: 0,
        width: '100%'
      })
    ]),
    query(':enter', [style({ left: '-100%' })]),
    query(':leave', animateChild()),
    group([
      query(':leave', [animate('300ms ease-out', style({ left: '100%' }))]),
      query(':enter', [animate('300ms ease-out', style({ left: '0%' }))])
    ]),
    query(':enter', animateChild())
  ]),
  transition('* <=> FilterPage', [
    style({ position: 'relative' }),
    query(':enter, :leave', [
      style({
        position: 'absolute',
        top: 0,
        left: 0,
        width: '100%'
      })
    ]),
    query(':enter', [style({ left: '-100%' })]),
    query(':leave', animateChild()),
    group([
      query(':leave', [animate('200ms ease-out', style({ left: '100%' }))]),
      query(':enter', [animate('300ms ease-out', style({ left: '0%' }))])
    ]),
    query(':enter', animateChild())
  ])
]);

export const fadeSlideInOut = trigger('fadeSlideInOut', [
  transition(':enter', [
    style({ opacity: 0, transform: 'translateY(10px)' }),
    animate('500ms', style({ opacity: 1, transform: 'translateY(0)' }))
  ]),
  transition(':leave', [animate('500ms', style({ opacity: 0, transform: 'translateY(10px)' }))])
]);
export const listaAnimation = trigger('listaAnimation', [
  transition(':enter, * => 0, * => -1', []),
  transition(':increment', [
    query(':enter', [style({ opacity: 0, width: '0px' }), stagger(50, [animate('300ms ease-out', style({ opacity: 1, width: '*' }))])], {
      optional: true
    })
  ]),
  transition(':decrement', [query(':leave', [stagger(50, [animate('300ms ease-out', style({ opacity: 0, width: '0px' }))])])])
]);

export const flyInOut = trigger('flyInOut', [
  transition(':enter', [
    style({ opacity: 0, transform: 'translateX(10px)' }),
    animate('1000ms', style({ opacity: 1, transform: 'translateX(0)' }))
  ]),
  transition(':leave', [animate('1000ms', style({ opacity: 0, transform: 'translateY(10px)' }))])
]);
/*
Copyright 2017-2018 Google Inc. All Rights Reserved.
Use of this source code is governed by an MIT-style license that
can be found in the LICENSE file at http://angular.io/license
*/

export const slide = trigger('slideStatus', [
  state('inactive', style({ backgroundColor: 'blue' })),
  state('active', style({ backgroundColor: 'orange' })),

  transition('* => active', [
    animate(
      '2s',
      keyframes([
        style({ backgroundColor: 'blue', offset: 0 }),
        style({ backgroundColor: 'red', offset: 0.8 }),
        style({ backgroundColor: 'orange', offset: 1.0 })
      ])
    )
  ]),
  transition('* => inactive', [
    animate(
      '2s',
      keyframes([
        style({ backgroundColor: 'orange', offset: 0 }),
        style({ backgroundColor: 'red', offset: 0.2 }),
        style({ backgroundColor: 'blue', offset: 1.0 })
      ])
    )
  ]),

  transition('* => active', [
    animate('2s', keyframes([style({ backgroundColor: 'blue' }), style({ backgroundColor: 'red' }), style({ backgroundColor: 'orange' })]))
  ])
]);

export const desdeDerecha = trigger('flyDesdeDerecha', [
  state('in', style({ transform: 'translateX(0)' })),
  transition('void => *', [style({ opacity: 0, transform: 'translateX(100%)' }), animate(1000)]),
  transition('* => void', [animate(5000, style({ transform: 'translateX(-100%)' }))])
]);

export const desdeIzquierda = trigger('flyDesdeIzquierda', [
  state('in', style({ transform: 'translateX(0)' })),
  transition('void => *', [style({ opacity: 0, transform: 'translateX(-100%)' }), animate(1000)]),
  transition('* => void', [animate(5000, style({ transform: 'translateX(100%)' }))])
]);

export const desdeArriba = trigger('flyDesdeArriba', [
  state('in', style({ transform: 'translateY(0)' })),
  transition('void => *', [style({ opacity: 0, transform: 'translateY(-100%)' }), animate(1000)]),
  transition('* => void', [animate(5000, style({ transform: 'translateY(100%)' }))])
]);

export const desdeAbajo = trigger('flyDesdeAbajo', [
  state('in', style({ transform: 'translateY(0)' })),
  transition('void => *', [style({ opacity: 0, transform: 'translateY(100%)' }), animate(1000)]),
  transition('* => void', [animate(5000, style({ transform: 'translateY(-100%)' }))])
]);

export const fadeInGrow = trigger('fadeInGrow', [
  transition(':enter', [
    query(':enter', [
      style({ opacity: 0, transform: 'scale(0.8)' }),
      sequence([
        animate('1000ms', style({ opacity: 1 })),
        animate('500ms ease-in-out', style({ transform: 'scale(1)' })),
        animate('500ms', style({ transform: 'scale(0.5)' })),
        animate('500ms', style({ transform: 'scale(0.8)' })),
        animate('500ms', style({ transform: 'scale(0.5)' })),
        animate('500ms', style({ transform: 'scale(1.3)' })),
        animate(
          '1000ms 200ms',
          keyframes([
            style({
              transform: 'perspective(400px) scale3d(1, 1, 1) translate3d(0, 0, 0) rotate3d(0, 1, 0, -360deg)',
              easing: 'ease-out',
              offset: 0
            }),
            style({
              transform: 'perspective(400px) scale3d(1, 1, 1) translate3d(0, 0, 150px) rotate3d(0, 1, 0, -190deg)',
              easing: 'ease-out',
              offset: 0.4
            }),
            style({
              transform: 'perspective(400px) scale3d(1, 1, 1) translate3d(0, 0, 150px) rotate3d(0, 1, 0, -170deg)',
              easing: 'ease-out',
              offset: 0.5
            }),
            style({
              transform: 'perspective(400px) scale3d(1.3, 1.3, 1.3) translate3d(0, 0, 0) rotate3d(0, 1, 0, 0deg)',
              easing: 'ease-in',
              offset: 0.8
            }),
            style({
              transform: 'perspective(400px) scale3d(1.3, 1.3, 1.3) translate3d(0, 0, 0) rotate3d(0, 1, 0, 0deg)',
              easing: 'ease-in',
              offset: 1
            })
          ])
        ),
        animate('500ms', style({ transform: 'scale(1)' }))
      ])
    ])
  ])
]);

export const voltear = trigger('voltear', [
  transition(':enter', [
    animate(
      '1000ms 500ms',
      keyframes([
        style({
          transform: 'perspective(400px) scale3d(1, 1, 1) translate3d(0, 0, 0) rotate3d(0, 1, 0, -360deg)',
          easing: 'ease-out',
          offset: 0
        }),
        style({
          transform: 'perspective(400px) scale3d(1, 1, 1) translate3d(0, 0, 150px) rotate3d(0, 1, 0, -190deg)',
          easing: 'ease-out',
          offset: 0.4
        }),
        style({
          transform: 'perspective(400px) scale3d(1, 1, 1) translate3d(0, 0, 150px) rotate3d(0, 1, 0, -170deg)',
          easing: 'ease-out',
          offset: 0.5
        }),
        style({
          transform: 'perspective(400px) scale3d(1, 1, 1) translate3d(0, 0, 0) rotate3d(0, 1, 0, 0deg)',
          easing: 'ease-in',
          offset: 0.8
        }),
        style({
          transform: 'perspective(400px) scale3d(1, 1, 1) translate3d(0, 0, 0) rotate3d(0, 1, 0, 0deg)',
          easing: 'ease-in',
          offset: 1
        })
      ])
    ),
    animate('500ms', style({ transform: 'scale(1)' }))
  ])
]);

export const idaVuelta = trigger('idaVuelta', [
  state('in', style({ transform: 'translateX(0)' })),
  transition('void => *', [style({ opacity: 0, transform: 'translateX(-100%)' }), animate(1000)]),
  transition('* => void', [animate(5000, style({ transform: 'translateX(100%)' }))])
]);
