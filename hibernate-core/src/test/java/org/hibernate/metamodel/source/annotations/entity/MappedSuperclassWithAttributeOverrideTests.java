/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2010, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.hibernate.metamodel.source.annotations.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.junit.Test;

import org.hibernate.action.internal.EntityIdentityInsertAction;
import org.hibernate.metamodel.binding.AttributeBinding;
import org.hibernate.metamodel.binding.EntityBinding;
import org.hibernate.metamodel.binding.EntityIdentifier;
import org.hibernate.metamodel.domain.NonEntity;
import org.hibernate.metamodel.domain.Superclass;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Tests for {@code j.p.AttributeOverrides} and {@code j.p.AttributeOverride}.
 *
 * @author Hardy Ferentschik
 */
public class MappedSuperclassWithAttributeOverrideTests extends BaseAnnotationBindingTestCase {
	@Test
	public void testMappedSuperclass() {
		buildMetadataSources( MyMappedSuperClass.class, MyEntity.class );
		EntityBinding binding = getEntityBinding( MyEntity.class );
		assertEquals( "Wrong entity name", MyEntity.class.getName(), binding.getEntity().getName() );
		assertEquals(
				"Wrong entity name",
				MyMappedSuperClass.class.getName(),
				binding.getEntity().getSuperType().getName()
		);
		assertTrue( binding.getEntity().getSuperType() instanceof Superclass );
		AttributeBinding nameBinding = binding.getAttributeBinding( "name" );
		assertNotNull( "the name attribute should be bound to the subclass", nameBinding );

		AttributeBinding idBinding = binding.getEntityIdentifier().getValueBinding();
		assertNotNull( "the id attribute should be bound", idBinding );
	}

	@Test
	public void testNoEntity() {
		buildMetadataSources( SubclassOfNoEntity.class, NoEntity.class );
		EntityBinding binding = getEntityBinding( SubclassOfNoEntity.class );
		assertEquals( "Wrong entity name", SubclassOfNoEntity.class.getName(), binding.getEntity().getName() );
		assertEquals( "Wrong entity name", NoEntity.class.getName(), binding.getEntity().getSuperType().getName() );
		assertTrue( binding.getEntity().getSuperType() instanceof NonEntity );
	}

	@MappedSuperclass
	class MyMappedSuperClass {
		@Id
		private int id;
		String name;
		int age;
	}

	@Entity
	class MyEntity extends MyMappedSuperClass {
		private Long count;
	}

	class NoEntity {
		String name;
		int age;
	}

	@Entity
	class SubclassOfNoEntity extends NoEntity {
		@Id
		private int id;
	}
}


